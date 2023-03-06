import unittest

from account import Account


class TestAccount(unittest.TestCase):
    def test_transfer(self):
        source = Account()
        source.deposit(200)

        destination = Account()
        destination.deposit(150)

        source.transfer_funds(destination, 100)

        self.assertEqual(250, destination.balance(), "Incorrect destination balance!")
        self.assertEqual(100, source.balance(), "Incorrect source balance!")

    def test_transfer_with_insufficient_funds_atomicity(self):
        source = Account()
        source.deposit(200)

        destination = Account()
        destination.deposit(150)

        try:
            source.transfer_funds(destination, 300)
        except Exception as e:
            # print(e)
            pass

        self.assertEqual(150, destination.balance(), "Incorrect destination balance!")
        self.assertEqual(200, source.balance(), "Incorrect source balance!")


# run the test
# unittest.main()
