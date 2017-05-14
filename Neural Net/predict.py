from Learn import  build_model
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.externals import joblib
from scipy.misc import imresize
import pickle
import numpy as np

from PrepareData import clear_text

with open('inverse_index.pickle', 'rb') as f:
    index = pickle.load(f)

vectorizer = joblib.load('vectorizer.pkl')

model = build_model(len(index))
model.load_weights('./weights.hdf5')

def predict(image, text):
    # Process image

    image_to_learn = np.array( [imresize(image, (100, 100)) / 255] )
    text_to_learn = np.array( [vectorizer.transform([clear_text(text)]).toarray()[0]] )

    prediction = model.predict([image_to_learn, text_to_learn])

    labels = [index[i].replace('_', ' -> ') for i in range(len(index))]
    result = sorted(zip(labels, prediction[0]))

    return result[:3]

if __name__ == '__main__':
    img = 'dataset/Women clothes_Jeans_Denim shorts/0ccdab4e-b87b-4aa8-a811-e66775227c69.jpg'