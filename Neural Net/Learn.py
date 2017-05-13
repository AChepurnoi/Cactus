from keras import applications, Input
from keras.models import Sequential, Model
from keras.layers import Dropout, Flatten, Dense, Merge
from keras.callbacks import ModelCheckpoint
from DataGen import ImageDataGenerator
import pickle

vec_size = 200
n_classes = 10

def build_model():
    input_tensor = Input(shape=(100, 100, 3))

    model = applications.VGG16(weights='imagenet', include_top=False, input_tensor=input_tensor)
    # Disable learning
    for layer in model.layers:
        if hasattr(layer, 'trainable'):
            layer.trainable = False

    top_model = Sequential()
    top_model.add(Flatten(input_shape=model.output_shape[1:], name='top_flatter'))
    top_model.add(Dense(256, activation='relu', name='top_relu'))
    top_model.add(Dropout(0.5))
    top_model.add(Dense(256, activation='sigmoid', name='top_sigmoid'))

    model = Model(input=model.input, output=top_model(model.output))

    words_model = Sequential()
    words_model.add(Dense(vec_size, input_shape=(vec_size,), name='words_input'))

    merged = Merge([model, words_model], mode='concat')

    final_model = Sequential()
    final_model.add(merged)
    final_model.add(Dropout(0.5))
    final_model.add(Dense(n_classes, activation='softmax'))

    final_model.compile(loss='categorical_crossentropy',
                        optimizer='adadelta',
                        metrics=['accuracy'])

    return final_model

def train_model():

    model = build_model()

    datagen = ImageDataGenerator(rescale=1. / 255)

    train_generator = datagen.flow_from_directory(
            'dataset',
            target_size=(100, 100),
            batch_size=32,
            class_mode='categorical')

    inverse_index = {train_generator.class_indices[i]: i for i in train_generator.class_indices.keys()}
    pickle.dump(inverse_index, 'inverse_index.pickle')

    checkpointer = ModelCheckpoint(filepath="./weights.hdf5", verbose=1, save_best_only=False)
    model.fit_generator(train_generator, 5, epochs=100000, callbacks=[checkpointer])

if __name__ == '__main__':
    train_model()