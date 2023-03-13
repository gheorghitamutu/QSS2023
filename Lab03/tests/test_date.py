import unittest
import datetime

from date import Date


class TestDate(unittest.TestCase):
    def test_distance(self):
        initial = Date(year=2000, month=12, day=12)
        current = Date(year=2002, month=12, day=12)

        delta = initial.distance(current)

        self.assertEqual(datetime.timedelta(days=-730), delta, "Incorrect delta time!")

    def test_exception(self):
        self.assertRaises(ValueError, Date(year=2000, month=13, day=12))


# run the test
# unittest.main()
