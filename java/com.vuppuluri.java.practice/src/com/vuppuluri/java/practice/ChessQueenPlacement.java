package com.vuppuluri.java.practice;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class ChessQueenPlacement {
	int boardDimension = 0;

	void printBoard(int[][] board) {
		for (int i = 0; i < this.boardDimension; i++) {
			for (int j = 0; j < this.boardDimension; j++)
				System.out.print(board[i][j] + " ");
			System.out.println();
		}
	}

	boolean ensureNoConflict(int[][] board, int row, int col) {

		for (int i = 0; i < col; i++) {
			if (board[row][i] == 1)
				return false;
		}

		for (int i = row, j = col; i >= 0 && j >= 0; i--, j--)
			if (board[i][j] == 1)
				return false;

		for (int i = row, j = col; i < this.boardDimension && j >= 0; i++, j--)
			if (board[i][j] == 1)
				return false;

		return true;

	}

	boolean solutionExists(int[][] board, int dim) {
		if (dim >= this.boardDimension)
			return true;
		for (int i = 0; i < this.boardDimension; i++) {
			if (ensureNoConflict(board, i, dim)) {
				board[i][dim] = 1;
				if (solutionExists(board, dim + 1)) {
					return true;
				}
				board[i][dim] = 0;
			}
		}
		return false;
	}

	boolean solveQueen(int N) {

		int board[][] = new int[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < 4; j++)
				board[i][j] = 0;
		}
		this.boardDimension = N;
		printBoard(board);
		if (solutionExists(board, 0)) {
			System.out.println("Found solution");
			printBoard(board);
		} else {
			System.out.println("Solution not found");
			return false;
		}
		return true;
	}

	public static void main(String args[]) throws IOException {
		int numSquares = 0;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		numSquares = Integer.parseInt(br.readLine());
		System.out.println("initializing board with dimensions: " + numSquares);
		ChessQueenPlacement Queen = new ChessQueenPlacement();
		Queen.solveQueen(numSquares);
	}

}
