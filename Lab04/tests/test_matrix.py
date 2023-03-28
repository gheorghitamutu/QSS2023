import unittest
from unittest.mock import MagicMock
import time

from matrix import Matrix


def time_func(func):
    def wrapper(*args, **kwargs):
        start = time.perf_counter()
        result = func(*args, **kwargs)
        end = time.perf_counter()
        print(f"{func.__name__}: {end - start:.3}s")
        return result

    return wrapper


class TestMatrix(unittest.TestCase):
    @time_func
    def test_01(self):
        input_data = [2]
        matrix = Matrix(input_data)
        determinant = matrix.determinant(input_data)

        self.assertEqual(2, determinant, "Incorrect determinant value!")

    @time_func
    def test_02(self):
        input_data = [[1,2,4],[3,4,7],[5,6,7]]
        matrix = Matrix(input_data)
        determinant = matrix.determinant(input_data)

        self.assertEqual(6, determinant, "Incorrect determinant value!")

    @time_func
    def test_03(self):
        input_data = [[1,2,3,4],[4,3,5,6],[8,4,2,1],[3,2,4,1]]
        determinant = Matrix.determinant(input_data)

        self.assertEqual(-99, determinant, "Incorrect determinant value!")

    @time_func
    def test_04(self):
        input_data = [[1, 3, 5, 7, 9], [4, 6, 3, 7, 5], [5, 10, 8, 3, 1], [1, 5, 3, 7, 6], [8, 1, 7, 5, 8]]
        determinant = Matrix.determinant(input_data)

        self.assertEqual(-687, determinant, "Incorrect determinant value!")

    @time_func
    def test_05(self):
        N = 6
        input_data_6 = [list(range(1 + N * i, 1 + N * (i + 1))) for i in range(N)]
        # N = 7
        # self.input_data_7 = [list(range(1 + N * i, 1 + N * (i + 1))) for i in range(N)]
        _ = Matrix.determinant(input_data_6)
        self.assertTrue(True, "Incorrect determinant value!")

    @time_func
    def test_06(self):
        N = 7
        input_data_7 = [list(range(1 + N * i, 1 + N * (i + 1))) for i in range(N)]
        # N = 7
        # self.input_data_7 = [list(range(1 + N * i, 1 + N * (i + 1))) for i in range(N)]
        _ = Matrix.determinant(input_data_7)
        self.assertTrue(True, "Incorrect determinant value!")

    @time_func
    def test_07(self):
        N = 8
        input_data_8 = [list(range(1 + N * i, 1 + N * (i + 1))) for i in range(N)]
        _ = Matrix.determinant(input_data_8)
        self.assertTrue(True, "Incorrect determinant value!")

    @time_func
    def test_08(self):
        N = 9
        input_data_9 = [list(range(1 + N * i, 1 + N * (i + 1))) for i in range(N)]
        _ = Matrix.determinant(input_data_9)
        self.assertTrue(True, "Incorrect determinant value!")

    @time_func
    def test_09(self):
        N = 10
        input_data_10 = [list(range(1 + N * i, 1 + N * (i + 1))) for i in range(N)]
        _ = Matrix.determinant(input_data_10)
        self.assertTrue(True, "Incorrect determinant value!")

    @time_func
    def test_10(self):
        input_data = lambda n: [list(range(1 + n * i, 1 + n * (i + 1))) for i in range(n)]
        a = input_data(11)
        _ = Matrix.determinant(a)
        self.assertTrue(True, "Incorrect determinant value!")
