from Learn import  build_model
from sklearn.externals import joblib
import pickle

from PrepareData import clear_text

model = build_model()
model.load_weights('./weights.hdf5')

index = pickle.load('inverse_index.pickle')
vectorizer = joblib.load('vectorizer.pkl')

def predict(image, text):
    # Process image

    
    text_to_learn = vectorizer.transform(vectorizer.transform([clear_text(text)]).toarray()[0])
    prediction = model.predict([image, text_to_learn])

