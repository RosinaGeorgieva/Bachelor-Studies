#ifndef OPERATION_H_
#define OPERATION_H_
#include <iostream>
#include "search_tree.h"

using namespace std;
class Operation
{
private:
	string operation_;
public:
	Operation(string = "insert");

	bool DoOperation(BinaryOrderedTree&);

	string GetOperation() const;

	friend istream& operator>>(istream&, Operation&);
};


istream & operator>>(istream & is, Operation & op)
{
	is >> op.operation_;
	return is;
}

Operation::Operation(string op)
{
	operation_ = op;
}

bool Operation::DoOperation(BinaryOrderedTree& tree)
{
	Student s;
	if (operation_ == "insert")
	{
		cin >> s;
		tree.AddNode(s);
		cout << "Record inserted! \n";
		return 1;
	}
	else if (operation_ == "delete")
	{
		unsigned long long fac_num;
		cin >> fac_num;
		if (tree.DeleteNode(fac_num))
		{
			cout << "Record deleted! \n";
		}
		else
		{
			cout << "Record not found! \n";
		}
		return 1;
	}
	else if (operation_ == "find")
	{
		unsigned long long fac_num;
		cin >> fac_num;
		tree.Find(fac_num);
		return 1;
	}
	else if (operation_ == "traverseInorder")
	{
		tree.TraverseInorder();
		cout << endl;
		return 1;
	}
	else return 0;
}

string Operation::GetOperation() const
{
	return operation_;
}


#endif
