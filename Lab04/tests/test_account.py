import traceback
import unittest
from unittest.mock import MagicMock

from account import Account
from currency_service import CurrencyService


class TestAccount(unittest.TestCase):
    def test_transfer(self):
        currency_service = CurrencyService()
        currency_service.get_conversion_rate = MagicMock(
            name='get_conversion_rate',
            args=['EUR', 'EUR'],
            return_value=1.0)

        source = Account(currency_service)
        source.deposit(200.0)

        destination = Account(currency_service)
        destination.deposit(150.0)

        source.transfer_funds(destination, 100.0)

        self.assertEqual(250.0, destination.balance(), "Incorrect destination balance!")
        self.assertEqual(100.0, source.balance(), "Incorrect source balance!")

    def test_transfer_with_insufficient_funds_atomicity(self):
        currency_service = CurrencyService()
        currency_service.get_conversion_rate = MagicMock(
            name='get_conversion_rate',
            args=['EUR', 'EUR'],
            return_value=1.0)

        source = Account(currency_service)
        source.deposit(200.0)

        destination = Account(currency_service)
        destination.deposit(150.0)

        try:
            source.transfer_funds(destination, 300.0)
        except Exception as e:
            print(e)
            traceback.print_exc()
            pass

        self.assertEqual(150.0, destination.balance(), "Incorrect destination balance!")
        self.assertEqual(200.0, source.balance(), "Incorrect source balance!")

    def test_transfer_with_different_currency(self):
        currency_service = CurrencyService()
        currency_service.get_conversion_rate = MagicMock(
            name='get_conversion_rate',
            args=['EUR', 'CAD'],
            return_value=1.47)

        italian_account = Account(currency_service)
        italian_account.set_currency('EUR')
        italian_account.deposit(200.0)

        canadian_account = Account(currency_service)
        canadian_account.set_currency('CAD')
        canadian_account.deposit(150.0)

        italian_account.transfer_funds(canadian_account, 100.0)

        self.assertEqual('EUR', italian_account.get_currency(), "Incorrect currency!")
        self.assertEqual('CAD', canadian_account.get_currency(), "Incorrect currency!")

        self.assertEqual(53.0, italian_account.balance(), "Incorrect destination balance!")
        self.assertEqual(297.0, canadian_account.balance(), "Incorrect source balance!")


# run the test
# unittest.main()
