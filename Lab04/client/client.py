class Client:
    def __init__(self, client_id, database, currency):
        self.database = database
        self.client_id = client_id
        self.currency = currency

    def total_amount(self):
        amount = 0

        for account in self.database.fetch_account(self.client_id):
            rate = self.database.fetch_rate(account.get_currency(), self.currency)
            amount += account.balance() * rate

        return amount
