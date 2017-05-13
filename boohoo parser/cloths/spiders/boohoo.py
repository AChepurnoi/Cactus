# -*- coding: utf-8 -*-
import scrapy
from scrapy.spiders import CrawlSpider, Rule
from scrapy.linkextractors import LinkExtractor
from scrapy.loader import ItemLoader
from ..items import ClothsItem


class BoohooSpider(CrawlSpider):
    name = "boohoo"
    allowed_domains = ['www.boohoo.com']
    start_urls = ["http://www.boohoo.com/"]
    rules = (
        Rule(LinkExtractor(allow=('.*color=.*')),
            callback='parse_image'),
        Rule(LinkExtractor(allow=('/(kids|womens|mans)/.*'), deny=('.*/.*\?.*')),
            follow=True),
        Rule(LinkExtractor(allow=('.*/.*\?.*start=.*')),
            follow=True),
    )

    def parse_image(self, response):
        print(response.url)
        l = ItemLoader(item=ClothsItem(), response=response)

        l.add_value('url', response.url)
        l.add_xpath('title', 'normalize-space(//*[@id="pdpMain"]/div[2]/h1)')
        l.add_xpath('price', '//*[@id="product-content"]/div[2]/span/text()')
        l.add_xpath('description', '//*[@id="product-content"]/ul/li[1]/div/p/text()')
        l.add_xpath('detail', 'normalize-space(//*[@id="product-content"]/ul/li[2]/div/text())')
        l.add_xpath('image_url', '//*[@id="product-main-zoom"]/@href')
        l.add_xpath('category', '//*[@id="main"]/ol/li[position()>1]/a/text()')
        return l.load_item()