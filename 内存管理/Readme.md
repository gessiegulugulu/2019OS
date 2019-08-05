# 说明文档
## 一	设计
### 1.	项目需求
(1)	初始态下，可用的内存空间为640k。

(2)	请求队列如下：
- 作业1申请130K；
- 作业2申请60K；
- 作业3申请100k；
- 作业2释放60K；
- 作业4申请200K；
- 作业3释放100K；
- 作业1释放130K；
- 作业5申请140K；
- 作业6申请60K；
- 作业7申请50K；
- 作业6释放60K。

(3)	分别用首次适应算法和最佳适应算法进行内存块的分配和回收。

(4)	显示每次分配和回收之后的空闲分区链情况。

### 2.	分析
(1)	首次适应算法：该算法是按分区的前后次序，从表首开始顺序查找，直到找到符合要求的第一个空闲分区。

(2)	最佳适应算法：该算法是在所有符合要求的空闲分区中，找到最小的分区分配。
### 3.  逻辑结构
(1)	分区分配函数：

首次适应算法和最佳适应算法都是选择符合要求的第一个分区，不同的是，二者选取空闲分区的列表不同。首次适应算法选取的空闲分区列表，是按照空闲分区的首地址顺序由小到大排列，组成的列表。而最佳适应算法选取的空闲分区列表，是按照空闲分区的大小顺序由小到大排列，从而组成的列表。
所以我们只需要实现二者不同的排序规则，在执行所选算法之前，对存储空闲分区的列表进行对应排序。则分区分配函数只需在排序后的列表中选择符合要求的第一个分区即可。

(2)	释放函数

当一个作业需要释放空间时，需要执行下列操作：

-	在占用列表中找到该作业的位置，在占用列表中删除对应空间。
-	在空闲列表中存入对应空间。
-	按照首地址从小到大的顺序对空闲列表进行排序，合并相邻的空闲列表为一个。

## 二	实现
### 1.	核心变量声明
以列表形式存储着不同类型的内存和作业，具体如下：
```
vector<space> free_list	空闲内存列表
vector<space> used_list	占用内存列表
vector<space> list	内存列表
queue<ready_process> ready_list	就绪的作业队列
queue<ready_process> wait_list	等待的作业队列
```
### 2.	首次适应算法排序
按照首地址由小到大的顺序排序，代码如下：
```
1.		int cmp_first(space a, space b)  
2.	{  
3.	    return a.StartAddress < b.StartAddress;  
4.	}  
```
### 3.	最佳适应算法排序
按照块由小到大排序，如果块大小相同，按照首地址顺序排序，代码如下：
```
1.		int cmp_best(space a, space b)  
2.	{  
3.	    if (a.len < b.len)  
4.	        return 1;  
5.	    if (a.len == b.len && a.StartAddress < b.StartAddress)  
6.	        return 1;  
7.	    return 0;  
8.	}  
```
### 4.	选择符合要求的第一个空闲分区
遍历排序后的空闲分区列表，将第一个长度大于当前作业的分区分配给当前作业。将这部分空间存入占用列表中，并在空闲列表中删除这部分空间。若未能找到合适的 空闲分区，则将当前作业放入就绪列表中，等待下次处理。代码如下：
```
1.		void Alloc(ready_process Process)  
2.	{  
3.	    space FreeSpace;  
4.	    bool match = 0;  
5.	    vector<space>::iterator it;  
6.	    for (it = free_list.begin(); it != free_list.end(); ++it)  
7.	    {  
8.	        if (((*it).len) >= Process.size)  
9.	        {  
10.	            FreeSpace.len = Process.size;  
11.	            FreeSpace.StartAddress = (*it).StartAddress;  
12.	            FreeSpace.id = Process.id;  
13.	  
14.	            used_list.push_back(FreeSpace);  
15.	            (*it).StartAddress += Process.size;  
16.	            (*it).len -= Process.size;  
17.	            if ((*it).len == 0)  
18.	            {  
19.	                free_list.erase(it);  
20.	            }  
21.	            match = 1;  
22.	            break;  
23.	        }  
24.	    }  
25.	    if (match == 0)  
26.	    {  
27.	        ready_list.push(Process);  
28.	    }  
29.	}  
```
### 5.	释放函数
在占用列表中找到该作业的位置，在占用列表中删除对应空间。并在空闲列表中存入该空间。再按照首地址从小到大的顺序对空闲列表进行排序，合并相邻的空闲列表。代码如下：
```
1.		void Free(ready_process Process)  
2.	{  
3.	    space FreeSpace;  
4.	    vector<space>::iterator it;  
5.	    for (it = used_list.begin(); it != used_list.end(); ++it)  
6.	    {  
7.	        if (((*it).id) == Process.id)  
8.	        {  
9.	            FreeSpace.StartAddress = (*it).StartAddress;  
10.	            FreeSpace.len = Process.size;  
11.	            free_list.push_back(FreeSpace);//空闲  
12.	  
13.	            (*it).len -= Process.size;  
14.	            if ((*it).len == 0)  
15.	            {  
16.	                used_list.erase(it);  
17.	            }  
18.	            break;  
19.	        }  
20.	    }  
21.	    sort(free_list.begin(), free_list.end(), cmp_first);  
22.	    for (int i = 0; i < free_list.size() - 1; i++)  
23.	    {  
24.	        if (free_list[i].StartAddress + free_list[i].len == free_list[i + 1].StartAddress)  
25.	        {  
26.	            free_list[i].len += free_list[i + 1].len;  
27.	            free_list.erase(free_list.begin() + i + 1);  
28.	        }  
29.	    }  
30.	  
31.	}  
```
### 6.	分区分配操作函数
先遍历就绪列表（之前未能成功装入的等待作业），依次执行首次适应算法或者最佳适应算法。再遍历等待列表，按照作业类别进行空间申请或释放。代码如下：
```
1.		void Oper(int option)  
2.	{  
3.	    ready_process Process;  
4.	    while (!ready_list.empty()) //未成功装入  
5.	    {  
6.	        Process = ready_list.front();  
7.	        ready_list.pop();  
8.	        if (option == 1)  
9.	        {  
10.	            Alloc_first(Process);  
11.	        }  
12.	        else if (option == 2)  
13.	        {  
14.	            Alloc_best(Process);  
15.	        }  
16.	    }  
17.	    while (!wait_list.empty())  
18.	    {  
19.	        Process = wait_list.front();  
20.	        wait_list.pop();  
21.	        if (Process.state == 1)  
22.	        {  
23.	            if (option == 1)  
24.	            {  
25.	                Alloc_first(Process);  
26.	            }  
27.	            else if (option == 2)  
28.	            {  
29.	                Alloc_best(Process);  
30.	            }  
31.	        }  
32.	        else  
33.	        {  
34.	            Free(Process);  
35.	        }  
36.	    }  
37.	}  
```
