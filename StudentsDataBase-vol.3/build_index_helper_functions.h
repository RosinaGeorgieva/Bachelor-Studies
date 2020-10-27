#ifndef BUILD_INDEX_HELPER_FUNCTIONS_H_
#define BUILD_INDEX_HELPER_FUNCTIONS_H_
#include <iostream>
#include <fstream>
#include <vector>
#include "student_struct.h"
#include "index_struct.h"

void Swap(Index* a, Index* b)
{
	Index t = *a;
	*a = *b;
	*b = t;
}

int Partition(vector<Index>& arr, int low, int high)
{
	Index pivot = arr[high];
	int i = (low - 1);

	for (int j = low; j <= high - 1; j++)
	{
		if (arr[j] <= pivot)
		{
			i++;
			Swap(&arr[i], &arr[j]);
		}
	}
	Swap(&arr[i + 1], &arr[high]);
	return (i + 1);
}

void QuickSort(vector<Index>& arr, int low, int high)
{
	if (low < high)
	{
		int pi = Partition(arr, low, high);
		QuickSort(arr, low, pi - 1);
		QuickSort(arr, pi + 1, high);
	}
}

void SortStudents(fstream& students_grades, vector<Index>& v)
{
	students_grades.open("StudentsGrades.db", ios::in);
	if (!students_grades) { cerr << "The file couldn't be opened!\n"; return; }
	students_grades.seekg(ios::beg);
	Student st;
	Index current;
	current.offset_ = students_grades.tellg();
	while (students_grades >> st)
	{
		current.fac_num_ = st.GetFacNum();
		v.push_back(current);
		current.offset_ = students_grades.tellg();
	}
	students_grades.close();
	int size = v.size() - 1;
	QuickSort(v, 0, size);
	return;
}

#endif