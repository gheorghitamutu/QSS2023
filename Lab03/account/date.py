import time
import datetime


class Date:
    def __init__(self, year, month, day):  # , hour, minute, seconds, milliseconds):
        self.value = datetime.date(year=year, month=month, day=day)
        # self.value.replace(hour=hour, minute=minute, seconds=seconds)

    def distance(self, date):
        return self.value - date.value
