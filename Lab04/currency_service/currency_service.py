class CurrencyService:
    def __init__(self):
        pass

    @staticmethod
    def get_conversion_rate(a, b):
        if a == b:
            return 1.0

        if a == 'EUR' and b == 'CAD':
            return 1.47

        if a == 'CAD' and b == 'EUR':
            return 0.68

        raise f'Not implemented {a} to {b} conversion!'
