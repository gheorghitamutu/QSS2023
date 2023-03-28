import copy

class Matrix:
    def __init__(self, matrix):
        self.matrix = matrix
        self.n = len(matrix)

    @staticmethod
    def new_matrix(a, i):  # FUNCTION TO FIND THE NEW MATRIX
        arr = copy.deepcopy(a)

        if len(arr) == 2:

            return arr
        else:
            arr.pop(0)
            for j in arr:
                j.pop(i)

            return arr

    @staticmethod
    def determinant(matrix):  # FUNCTION TO FIND THE DETERMINANT OF A MATRIX
        if len(matrix) == 1:
            pro = matrix[0]
            return pro

        elif len(matrix) == 2:
            pro = matrix[0][0] * matrix[1][1] - matrix[1][0] * matrix[0][1]
            return pro

        else:
            pro = 0
            for i in range(len(matrix[0])):
                pro += ((-1) ** i) * matrix[0][i] * Matrix.determinant(Matrix.new_matrix(matrix, i))
            return pro
