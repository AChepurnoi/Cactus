# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: http://doc.scrapy.org/en/latest/topics/item-pipeline.html
import json
import scrapy
from scrapy.pipelines.images import ImagesPipeline
from scrapy.exceptions import DropItem

class ClothsPipeline():
    def process_item(self, item, spider):
        return item

class ClearItem():

    def process_item(self, item, spider):
        item['description'] = ' '.join(item['description'])
        item['category'] = '|'.join(item['category'])
        item['image_url'] = 'http://' + item['image_url'][0][2:]
        item['detail'] = item['detail'][0]
        item['url'] = item['url'][0]
        item['title'] = item['title'][0]
        item['price'] = float(item['price'][0][1:])
        return item

class JsonWriterPipeline():
    def __init__(self):
        self.unique_id = 0

    def get_id(self):
        self.unique_id += 1
        return self.unique_id

    def open_spider(self, spider):
         self.file = open('items.json', 'w')

    def close_spider(self, spider):
        self.file.close()

    def process_item(self, item, spider):
        item['id'] = self.get_id()
        line = json.dumps(dict(item)) + "\n"
        self.file.write(line)
        return item