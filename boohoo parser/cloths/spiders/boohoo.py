# -*- coding: utf-8 -*-
import scrapy
from scrapy.spiders import CrawlSpider, Rule
from scrapy.linkextractors import LinkExtractor
from scrapy.loader import ItemLoader
from ..items import ClothsItem


class BoohooSpider(CrawlSpider):
    name = "jumia"
    allowed_domains = ['www.jumia.com.ng']
    start_urls = ["https://www.jumia.com.ng/men-clothing",
                  'https://www.jumia.com.ng/mens-shoes']
    rules = (
        Rule(LinkExtractor(allow=('.*-[0-9]*.html')),
            callback='parse_image'),
        Rule(LinkExtractor(allow=('.*/?page=[0-9]*')),
            follow=True),
    )

    def parse_image(self, response):
        print(response.url)
        print('***Title: ', response.xpath('/html/body/main/nav/ul/li[position()>5]/a/text()').extract())
        l = ItemLoader(item=ClothsItem(), response=response)

        l.add_value('url', response.url)
        l.add_xpath('title', '/html/body/main/nav/ul/li[position()>5]/a/text()')
        l.add_value('price', ' ')
        l.add_xpath('description', '//*[@id="productDescriptionTab"]/div/text()')
        l.add_value('detail', ' ')
        l.add_xpath('image_url', '/html/body/main/section[2]/section[2]/div[1]/a/div[1]/img/@data-src')
        l.add_xpath('category', '/html/body/main/nav/ul/li[position()>2 and position()<6]/a/text()')
        return l.load_item()
