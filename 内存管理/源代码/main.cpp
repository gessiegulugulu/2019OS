//1752095 ��˼��
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

#define AvailableSpace 640//�����ڴ�ռ�

//�����Ľ���
struct ready_process
{
	int id;//���̱��
	int state;//���̵�״̬��1 ���� 0 �ͷ�
	int size;//���̿ռ�
};

//�ڴ�
struct space
{
	int id;//��ҵ��
	int StartAddress;//�׵�ַ
	int len;//����
};

vector<space> free_list;//�����ڴ��
vector<space> used_list;//ռ���ڴ��
vector<space> list;//�ڴ�
queue<ready_process> ready_list;//��������ҵ���У�δ�ɹ�װ��ģ�
queue<ready_process> wait_list;//�ȴ�����ҵ����

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
{//��ʾ������������ռ�ñ����Ϣ
	list.insert(list.end(), free_list.begin(), free_list.end());
	list.insert(list.end(), used_list.begin(), used_list.end());
	sort(list.begin(), list.end(), cmp_first);//����֮ǰ���Ȱ��׵�ַ��С��������

	cout << "�X�T�T�T�T�T�h�T�T�T�T�T�h�T�T�T�T�T�[" << endl;
	cout << "�U ��ʼ��ַ �� ��ҵ��� �� ������ַ �U" << endl;

	for (int i = 0; i < list.size(); i++)
	{
		cout << "�c�����������੤���������੤���������f" << endl;
		//		cout << "�c----------��----------��----------�f" << endl;
		cout << "�U" << setw(10) << list[i].StartAddress << "��";
		if (list[i].id <= 0)
		{
			cout << setw(12) << "null��";
		}
		else
		{
			cout << setw(10) << list[i].id << "��";
		}
		cout << setw(10) << list[i].StartAddress + list[i].len << "�U" << endl;

	}
	cout << "�^�T�T�T�T�T�k�T�T�T�T�T�k�T�T�T�T�T�a" << endl;
	list.clear();
}

//��ʼ��
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

//�����Ӧ�㷨
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

	cout << "��ҵ" << Process.id << "����" << Process.size << "k,��ʱ�ڴ�ռ����£�" << endl;
	Show();
}

//�״���Ӧ�㷨
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

			used_list.push_back(FreeSpace);//��ռ��

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
	cout << "��ҵ" << Process.id << "����" << Process.size << "k,��ʱ�ڴ�ռ����£�" << endl;
	Show();
}

//�ͷ�
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
			free_list.push_back(FreeSpace);//����

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
	cout << "��ҵ" << Process.id << "�ͷ�" << Process.size << "k,��ʱ�ڴ�ռ����£�" << endl;
	Show();
}

void Oper(int option)
{
	ready_process Process;
	while (!ready_list.empty()) //δ�ɹ�װ��
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
	cout << "          ��ѡ����䷽��          " << endl;
	cout << "          1:�״���Ӧ�㷨          " << endl;
	cout << "          2:�����Ӧ�㷨          " << endl;
	cout << "==================================" << endl;
	cout << "ѡ��";
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

