import re
import os
import json
import shutil
import numpy as np
from nltk.corpus import stopwords
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.externals import joblib


def clear_text(text):
    letters_only = re.sub("[^a-zA-Z]", " ", text)
    lower_case = letters_only.lower()
    words = lower_case.split()
    words = ' '.join([w for w in words if not w in stopwords.words("english")])
    return words


def create_vectorizer(data, max_features=200):
    vectorizer = CountVectorizer(analyzer="word", tokenizer=None, preprocessor=None, stop_words=None, max_features=200)
    vectorizer.fit([clear_text(x['text']) for x in data])

    return vectorizer


def create_dirs(dest, dirnames):
    for name in dirnames:
        try:
            if not os.path.exists(os.path.join(dest, name)):
                os.makedirs(os.path.join(dest, name))
        except:
            pass


def record_to_cat(source, dest, records, vectorizer):
    i = 1
    for record in records:
        if not os.path.exists(os.path.join(source, record['id'] + '.jpg')):
            continue

        if not os.path.exists(os.path.join(dest, record['crumbs'])):
            os.makedirs(os.path.join(dest, record['crumbs']))

        shutil.copy(os.path.join(source, record['id'] + '.jpg'),
                    os.path.join(dest, record['crumbs'], record['id'] + '.jpg'))

        vector = vectorizer.transform([clear_text(record['text'])]).toarray()[0]
        np.save(os.path.join(dest, record['crumbs'], record['id'] + '.npy'), vector)

        if i % 100 == 0:
            print('Categorize: ', i)
        i += 1


if __name__ == '__main__':
    with open('data.json') as data_file:
        data = np.array(json.load(data_file))

    test_data = np.random.choice(data, 2000)

    vectorizer = create_vectorizer(test_data)

    filename = 'vectorizer.pkl'
    joblib.dump(vectorizer, filename)

    record_to_cat('final/images', 'dataset', test_data, vectorizer)