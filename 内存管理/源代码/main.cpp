//1752095 范思琪
#define _CRT_SECURE_NO_WARNINGS
#include<iostream>
#include<iomanip>
#include<cstdio>
#include<map>
#include<string>
#include<algorithm>
#include<vector>
#include<queue>
#include<cmath> 
using namespace std;

#define AvailableSpace 640//可用内存空间

//就绪的进程
struct ready_process
{
	int id;//进程编号
	int state;//进程的状态，1 申请 0 释放
	int size;//进程空间
};

//内存
struct space
{
	int id;//作业号
	int StartAddress;//首地址
	int len;//长度
};

vector<space> free_list;//空闲内存表
vector<space> used_list;//占用内存表
vector<space> list;//内存
queue<ready_process> ready_list;//就绪的作业队列（未成功装入的）
queue<ready_process> wait_list;//等待的作业队列

int cmp_first(space a, space b)
{
	return a.StartAddress < b.StartAddress;
}
int cmp_best(space a, space b)
{
	if (a.len < b.len)
		return 1;
	if (a.len == b.len && a.StartAddress < b.StartAddress)
		return 1;
	return 0;
}



void Show()
{//显示空闲区域表和已占用表的信息
	list.insert(list.end(), free_list.begin(), free_list.end());
	list.insert(list.end(), used_list.begin(), used_list.end());
	sort(list.begin(), list.end(), cmp_first);//操作之前首先按首地址从小到大排序

	cout << "╔═════╤═════╤═════╗" << endl;
	cout << "║ 开始地址 │ 作业序号 │ 结束地址 ║" << endl;

	for (int i = 0; i < list.size(); i++)
	{
		cout << "╟─────┼─────┼─────╢" << endl;
		//		cout << "╟----------┼----------┼----------╢" << endl;
		cout << "║" << setw(10) << list[i].StartAddress << "│";
		if (list[i].id <= 0)
		{
			cout << setw(12) << "null│";
		}
		else
		{
			cout << setw(10) << list[i].id << "│";
		}
		cout << setw(10) << list[i].StartAddress + list[i].len << "║" << endl;

	}
	cout << "╚═════╧═════╧═════╝" << endl;
	list.clear();
}

//初始化
void Init()
{
	space FreeSpace;
	FreeSpace.id = -1;
	FreeSpace.StartAddress = 0;
	FreeSpace.len = AvailableSpace;
	free_list.push_back(FreeSpace);

	ready_process Process;

	while (cin >> Process.id >> Process.state >> Process.size)
	{
		wait_list.push(Process);
	}
}

//最佳适应算法
void Alloc_best(ready_process Process)
{
	sort(free_list.begin(), free_list.end(), cmp_best);
	space FreeSpace;
	bool match = 0;
	vector<space>::iterator it;
	for (it = free_list.begin(); it != free_list.end(); ++it)
	{
		if (((*it).len) >= Process.size)
		{
			FreeSpace.len = Process.size;
			FreeSpace.StartAddress = (*it).StartAddress;
			FreeSpace.id = Process.id;

			used_list.push_back(FreeSpace);
			(*it).StartAddress += Process.size;
			(*it).len -= Process.size;
			if ((*it).len == 0)
			{
				free_list.erase(it);
			}
			match = 1;
			break;
		}
	}
	if (match == 0)
	{
		ready_list.push(Process);
	}

	cout << "作业" << Process.id << "申请" << Process.size << "k,此时内存空间如下：" << endl;
	Show();
}

//首次适应算法
void Alloc_first(ready_process Process)
{
	sort(free_list.begin(), free_list.end(), cmp_first);
	space FreeSpace;
	bool match = 0;
	vector<space>::iterator it;
	for (it = free_list.begin(); it != free_list.end(); ++it)
	{
		if (((*it).len) >= Process.size)
		{

			FreeSpace.len = Process.size;
			FreeSpace.StartAddress = (*it).StartAddress;
			FreeSpace.id = Process.id;

			used_list.push_back(FreeSpace);//已占用

			(*it).StartAddress += Process.size;
			(*it).len -= Process.size;
			if ((*it).len == 0)
			{
				free_list.erase(it);
			}
			match = 1;
			break;
		}
	}
	if (match == 0)
	{
		ready_list.push(Process);
	}
	cout << "作业" << Process.id << "申请" << Process.size << "k,此时内存空间如下：" << endl;
	Show();
}

//释放
void Free(ready_process Process)
{
	space FreeSpace;
	vector<space>::iterator it;
	for (it = used_list.begin(); it != used_list.end(); ++it)
	{
		if (((*it).id) == Process.id)
		{
			FreeSpace.StartAddress = (*it).StartAddress;
			FreeSpace.len = Process.size;
			free_list.push_back(FreeSpace);//空闲

			(*it).len -= Process.size;
			if ((*it).len == 0)
			{
				used_list.erase(it);
			}
			break;
		}
	}
	sort(free_list.begin(), free_list.end(), cmp_first);
	for (int i = 0; i < free_list.size() - 1; i++)
	{
		if (free_list[i].StartAddress + free_list[i].len == free_list[i + 1].StartAddress)
		{
			free_list[i].len += free_list[i + 1].len;
			free_list.erase(free_list.begin() + i + 1);
		}
	}
	cout << "作业" << Process.id << "释放" << Process.size << "k,此时内存空间如下：" << endl;
	Show();
}

void Oper(int option)
{
	ready_process Process;
	while (!ready_list.empty()) //未成功装入
	{
		Process = ready_list.front();
		ready_list.pop();
		if (option == 1)
		{
			Alloc_first(Process);
		}
		else if (option == 2)
		{
			Alloc_best(Process);
		}
	}
	while (!wait_list.empty())
	{
		Process = wait_list.front();
		wait_list.pop();
		if (Process.state == 1)
		{
			if (option == 1)
			{
				Alloc_first(Process);
			}
			else if (option == 2)
			{
				Alloc_best(Process);
			}
		}
		else
		{
			Free(Process);
		}
	}
}

int main()
{
	int option = 0;
	cout << "==================================" << endl;
	cout << "          请选择分配方法          " << endl;
	cout << "          1:首次适应算法          " << endl;
	cout << "          2:最佳适应算法          " << endl;
	cout << "==================================" << endl;
	cout << "选择：";
	cin >> option;
	cout << endl;

	freopen("input.txt", "r", stdin);
	//if (option == 1)
	//{
	//	freopen("output1.txt", "w", stdout);
	//}
	//else if (option == 2)
	//{
	//	freopen("output2.txt", "w", stdout);
	//}
	Init();
	Oper(option);



	return 0;
}

