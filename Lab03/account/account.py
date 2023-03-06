class Account:
    def __init__(self):
        self._balance = 0
        self._minimum_balance = 10

    def deposit(self, amount):
        self._balance += amount

    def withdraw(self, amount):
        self._balance -= amount

    def transfer_funds(self, destination, amount):
        if self._balance - amount < self._minimum_balance:
            raise Exception('Insufficient Funds Exception!')

        destination.deposit(amount)

        self.withdraw(amount)

    def balance(self):
        return self._balance

    def minimum_balance(self):
        return self._minimum_balance
