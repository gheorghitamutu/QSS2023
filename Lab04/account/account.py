class Account:
    def __init__(self, currency_service):
        self._balance = 0
        self._minimum_balance = 10
        self._currency = 'EUR'
        self._currency_service = currency_service

    def deposit(self, amount):
        self._balance += amount

    def withdraw(self, amount):
        self._balance -= amount

    def transfer_funds(self, destination, amount):

        if self._currency != destination.get_currency():
            conversion_rate = self._currency_service.get_conversion_rate(self._currency, destination.get_currency())
            amount = conversion_rate * amount

        if self._balance - amount < self._minimum_balance:
            raise Exception('Insufficient Funds Exception!')

        destination.deposit(amount)

        self.withdraw(amount)

    def balance(self, currency=None):
        if currency is None:
            return self._balance

        balance = self.balance()
        if self._currency != currency:
            conversion_rate = self._currency_service.get_conversion_rate(self._currency, currency)
            balance = conversion_rate * balance

        return balance


    def minimum_balance(self):
        return self._minimum_balance

    def set_currency(self, currency):
        self._currency = currency

    def get_currency(self):
        return self._currency

