from account import Account
from currency_service import CurrencyService


class DB:
    def __init__(self, currency_service):
        self._currency_service = currency_service

    def fetch_account(self, account_id):
        pass

    def fetch_rate(self, a, b):
        return self._currency_service.get_conversion_rate(a, b)

    def create_account(self):
        raise Exception('Not implemented!')
