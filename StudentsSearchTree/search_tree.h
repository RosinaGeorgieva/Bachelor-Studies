#ifndef SEARCH_TREE_H_
#define SEARCH_TREE_H_
#include <iostream>
#include "student_struct.h"

using namespace std;

struct Node
{
	Student data_;
	Node* left_;
	Node* right_;
};

class BinaryOrderedTree
{
private:
	Node* root_;

	void DeleteTree(Node* &) const;
	void Copy(Node*&, Node* const &) const;
	void CopyTree(BinaryOrderedTree const &);

	void PrintFromNode(const Node*,int) const;

	void Add(Node* &, Student const &) const;
	bool DeleteNodePtr(Node*&, const unsigned long long &);

	Node* FindNode(Node*, const unsigned long long&,bool) const;

	unsigned long long CountVertices(Node*) const;
	unsigned long long Count() const;
	
	void MinTree(Student&, BinaryOrderedTree&) const;
	void CreateWithData(Student const&, BinaryOrderedTree, BinaryOrderedTree);

	BinaryOrderedTree LeftTree() const;
	BinaryOrderedTree RightTree() const;
public:
	BinaryOrderedTree();
	BinaryOrderedTree(BinaryOrderedTree const &);
	BinaryOrderedTree& operator=(BinaryOrderedTree const &);
	~BinaryOrderedTree();

	void AddNode(Student const&);
	bool DeleteNode(const unsigned long long &);
	void TraverseInorder();
	bool Find(const unsigned long long&) const;

};

inline void BinaryOrderedTree::DeleteTree(Node*& tree_root) const
{
	if (!tree_root) return;
	DeleteTree(tree_root->left_);
	DeleteTree(tree_root->right_);
	delete tree_root;
	tree_root = nullptr;
	return;
}

inline void BinaryOrderedTree::Copy(Node*& node, Node* const & rhs_node) const
{
	node = nullptr;
	if (rhs_node) {
		node = new Node;
		node->data_ = rhs_node->data_;
		Copy(node->left_, rhs_node->left_);
		Copy(node->right_, rhs_node->right_);
	}
}

inline void BinaryOrderedTree::CopyTree(BinaryOrderedTree const & rhs)
{
	Copy(root_, rhs.root_);
}

inline void BinaryOrderedTree::PrintFromNode(const Node* node,int number_of_printed_vertices) const
{
	if (!node) return;
	number_of_printed_vertices += 1;
	PrintFromNode(node->left_,number_of_printed_vertices);
	cout << node->data_.fac_num_ ;
	if (number_of_printed_vertices < Count()) //тоест след всички върхове, освен последния, се принтира запетая
	{
		cout << ", ";
	}
	PrintFromNode(node->right_,number_of_printed_vertices);
}

inline void BinaryOrderedTree::Add(Node*& node, Student  const & x) const
{
	Node* temp = FindNode(root_, x.fac_num_,0);
	if (temp!=nullptr)
	{
		temp->data_ = x;
		return;
	}
	else
	{
		if (!node) {
			node = new Node;
			node->data_ = x;
			node->left_ = nullptr;
			node->right_ = nullptr;
			return;
		}
		if (node->data_ > x) {
			Add(node->left_, x);
		}
		else {
			Add(node->right_, x);
		}
	}
}

inline BinaryOrderedTree::BinaryOrderedTree()
{
	root_ = nullptr;
}

inline BinaryOrderedTree::BinaryOrderedTree(BinaryOrderedTree const & rhs)
{
	CopyTree(rhs);
}


inline BinaryOrderedTree& BinaryOrderedTree::operator=(BinaryOrderedTree const & rhs)
{
	if (this != &rhs)
	{
		DeleteTree(root_);
		CopyTree(rhs);
	}
	return *this;
}

inline BinaryOrderedTree::~BinaryOrderedTree()
{
	DeleteTree(root_);
}



inline BinaryOrderedTree BinaryOrderedTree::LeftTree() const
{
	BinaryOrderedTree left_;
	Copy(left_.root_, root_->left_);
	return left_;
}

inline BinaryOrderedTree BinaryOrderedTree::RightTree() const
{
	BinaryOrderedTree right_;
	Copy(right_.root_, root_->right_);
	return right_;
}


inline void BinaryOrderedTree::TraverseInorder()
{
	PrintFromNode(root_,0);
}

inline void BinaryOrderedTree::AddNode(Student const & x)
{
	Add(root_, x);
}

inline bool BinaryOrderedTree::DeleteNode(const unsigned long long & x)
{
	return DeleteNodePtr(root_, x);
}

inline bool BinaryOrderedTree::DeleteNodePtr(Node*& root, const unsigned long long & x)
{
	Node* temp = FindNode(root_, x , 0);
	if (temp == nullptr)
	{
		return 0;
	}
	if (!root) return 0;
	if (root->data_.fac_num_ > x) {
		DeleteNodePtr(root->left_, x);
	}
	else if (root->data_.fac_num_ < x) {
		DeleteNodePtr(root->right_, x);
	}
	else {
		Node *tempPtr;
		if (!(root->left_)) {
			tempPtr = root;
			root = root->right_;
			delete tempPtr;
		}
		else if (!(root->right_)) {
			tempPtr = root;
			root = root->left_;
			delete tempPtr;
		}
		else {
			tempPtr = root->right_;
			while (tempPtr->left_) {
				tempPtr = tempPtr->left_;
			}
			root->data_ = tempPtr->data_;
			DeleteNodePtr(root->right_, tempPtr->data_.fac_num_);
		}
	}
	return 1;
}

inline Node* BinaryOrderedTree::FindNode(Node * root, const unsigned long long& x,bool to_show) const
{
	if (!root)
	{
		if (to_show)
		{
		cout << "Record not found! \n";
		}
		return nullptr;
	}
	if (root->data_.fac_num_== x)
	{
		if (to_show)
		{
		cout << root->data_;
		}
		return root;
	}
	if (root->data_.fac_num_ > x)
	{
		FindNode(root->left_, x,to_show);
	}
	else
	{
		FindNode(root->right_, x,to_show);
	}
}

inline unsigned long long BinaryOrderedTree::CountVertices(Node * root) const
{
	if (!root) return 0;
	return 1 + CountVertices(root->left_) + CountVertices(root->right_);
}


inline void BinaryOrderedTree::CreateWithData(Student const & x, BinaryOrderedTree left_, BinaryOrderedTree right_)
{
	root_ = new Node;
	root_->data_ = x;
	Copy(root_->left_, left_.root_);
	Copy(root_->right_, right_.root_);
}

inline void BinaryOrderedTree::MinTree(Student & x, BinaryOrderedTree& min_tree) const
{
	Student a = root_->data_;
	if (!root_->left_) {
		x = a;
		min_tree = RightTree();
	}
	else {
		BinaryOrderedTree updatedLeftTree;
		LeftTree().MinTree(x, updatedLeftTree);
		min_tree.CreateWithData(a, updatedLeftTree, RightTree());
	}
}

inline bool BinaryOrderedTree::Find(const unsigned long long& x) const
{
	if (FindNode(root_, x,1) != nullptr)
	{
		return 1;
	}
	return 0;
}

inline unsigned long long BinaryOrderedTree::Count() const
{
	return CountVertices(root_);
}

#endif
