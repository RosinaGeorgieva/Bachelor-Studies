#ifndef UTIL_H_
#define UTIL_H_
#include <iostream>
#include <fstream>
#include <string>
#include <vector>
#include "student_struct.h"
#include "index_struct.h"
#include "build_index_helper_functions.h"

using namespace std;

bool SequentialSearch(fstream& students_grades, ull  fac_num)
{
	students_grades.open("StudentsGrades.db", ios::in);
	if (!students_grades) { cerr << "The file couldn't be opened!\n"; return 0; }
	students_grades.seekg(ios::beg);
	Student st;
	while (students_grades >> st)
	{
		if (st.GetFacNum() == fac_num)
		{
			
			cout << st;
			students_grades.close();
			return 1;
		}
	}
	cout << "Record not found! \n";
	students_grades.close();
	return 0;
}

bool BuildIndex(fstream& students_grades, fstream& fac_num_ids, fstream& students_number, bool to_show)
{
	///Сортиране на студентите заради бинарното търсене:
	vector<Index> temp;
	SortStudents(students_grades, temp);

	fac_num_ids.open("FacultyNumber.ids", ios::out|ios::binary);
	if (!fac_num_ids) { cerr << "The file couldn't be opened!\n"; return 0; }

	///Запис на брой на студентите в помощен файл:
	students_number.open("NumberOfStudents.txt", ios::out);
	if(!students_number) { cerr << "The file couldn't be opened!\n"; return 0; }
	students_number << temp.size();
	students_number.close();

	///Запис на студентите от сортирания вектор във файла индекс:
	int i = 0;
	while (i<temp.size())
	{
		fac_num_ids.write((char*)& temp[i], sizeof(Index));
		i++;
	}

	fac_num_ids.close();

	if (to_show == 1)
	{
		cout << "Index is built!" << endl;
	}
	return 1;
}

bool Search(fstream& students_grades, fstream& fac_num_ids, fstream& students_number, ull fac_num)
{
	fac_num_ids.open("FacultyNumber.ids", ios::in | ios::binary);
	///Ако не съществува индекс, се създава:
	if (!fac_num_ids.good())
	{
		fac_num_ids.close();
		BuildIndex(students_grades, fac_num_ids,students_number,0);
	}
	fac_num_ids.close();

	fac_num_ids.open("FacultyNumber.ids", ios::in | ios::binary);

	///Извличане на броя на студентите от помощния файл:
	int number_of_students;
	students_number.open("NumberOfStudents.txt", ios::in);
	students_number >> number_of_students;
	students_number.close();

	///Двоично търсене:
	int end = number_of_students-1; 
	int start = 0; 
	int middle = 0;

	Index ind; 
	while (start <= end)
	{
		middle = (start + end) / 2;

		fac_num_ids.seekg(middle * sizeof(Index));
		fac_num_ids.read((char*)&ind, sizeof(Index));

		if (ind.fac_num_>fac_num)
		{
			end = middle - 1;
		}
		else if (ind.fac_num_ < fac_num)
		{
			start = middle + 1;
		}
		else
		{
			students_grades.open("StudentsGrades.db", ios::in);
			students_grades.seekg(ind.offset_);
			Student st;
			students_grades >> st;
			cout << st;
			students_grades.close();
			fac_num_ids.close();
			return 1;
		}
	}
	fac_num_ids.close();
	cout << "Record not found! \n";
	return 0;
}

#endif