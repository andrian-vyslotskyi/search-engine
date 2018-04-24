import pykka
from kafka import KafkaProducer, KafkaConsumer
import time


class Producer(pykka.ThreadingActor):
    def __init__(self, servers):
        self.producer = KafkaProducer(bootstrap_servers=servers)

    def on_receive(self, message):
        # do some nn shit
        future = self.producer.send(message, message)
        future.get()


class Consumer():
    def __init__(self, servers, topic, group_id):
        self.consumer = KafkaConsumer(topic, bootstrap_servers=servers, group_id=group_id)

    def read_messages(self):
        for msg in self.consumer:
            print msg


if __name__ == '__main__':
    consumer = Consumer("", "", "")
    consumer.read_messages()
