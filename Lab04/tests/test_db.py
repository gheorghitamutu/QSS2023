import unittest
from unittest.mock import MagicMock

from account import Account
from currency_service import CurrencyService
from database import DB
from client import Client


class TestDatabase(unittest.TestCase):

    def test_fetch_account_from_db(self):
        currency_service = CurrencyService()

        accounts_for_client = list()
        for i in range(0, 10):
            account = Account(currency_service)
            account.deposit(i)
            account.set_currency('CAD')
            accounts_for_client.append(account)

        iterator = iter(accounts_for_client)

        db_accounts = DB(currency_service)
        db_accounts.fetch_account = MagicMock(
            name='fetch_account',
            args=['test'],
            return_value=iterator)

        client = Client('test', db_accounts, 'EUR')
        total_amount = client.total_amount()

        self.assertEqual(30.600000000000005, total_amount, "Incorrect account balance!")

# run the test
# unittest.main()
