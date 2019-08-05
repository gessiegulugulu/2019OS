# 项目说明文档
## 一、设计
### 1. 项目需求
(1)	某一栋楼有20层，和五部互联的电梯。基于线程思想，编写一个电梯调度程序，并考虑楼层和电梯数可以重新设置时应如何进行相似的电梯调度。

(2)	每层电梯门口设有上行和下行键，同一层不同电梯之间的上行和下行键相关联。

(3)	每个电梯内部都设有一些功能键：数字键、关门键、开门键、报警键。

(4)	存在可以显示电梯所在楼层数和上行或下行状态的数码显示器。

### 2. 分析
(1)	每个电梯都作为一个单独的线程存在，并接受外部控制按钮的调度。电梯的运行方式和是否响应请求，都取决于电梯当前的状态。对电梯状态进行编号：

- 电梯正在上行去响应一个向上的请求记为state=1；
- 电梯正在上行去响应一个向下的请求记为state=2；
- 电梯正在下行去响应一个向下的请求记为state=-1；
- 电梯正在下行去响应一个向上的请求记为state=-2；
- 电梯静止记为state=0；
- 电梯静止但所在楼层存在向上请求记为state=100；
- 电梯静止但所在楼层存在向下请求记为state=-100。
(2)	外部控制按钮接受每个楼层上行或下行的请求存入队列中，并按序将请求发送给电梯，调用适当电梯响应该请求。

(3)	每个电梯线程都包含电梯移动演示图和电梯内部功能键及数码显示器。

(4)	通过类来实现各部分的功能。

(5)	为了保证程序可以适用于可变的电梯数及楼层数，将楼层数和电梯数定义为宏，并将最后的显示界面放在可滚动的界面容器中。

(6)	为了方便界面布局的调节，将外部按钮的长宽、内部按钮的长宽设为宏。
### 3. 逻辑结构
    
## 二、实现
### 1. 功能实现
#### 1.1. 关键类的设计
(1)	insideView:

继承线程的Elev类：
 

(2)	outsideView

继承JPanel的Button类：
 


#### 1.2. 关键函数的实现
(1)	Elev 类中的upORdown() : 重新检测电梯状态（设此时电梯位于floor）
-	如果state=0且电梯内外有按钮按下了上到floor层，将state重置为100；
-	如果state=0且电梯内外有按钮按下了下到floor层，将state重置为-100；
-	如果state=0且不存在上述情况，从1到最高层检查需求数组，根据遇到的第一个需求设置state，然后检索请求队列并更新arrive、call数组；
-	如果state为1或2，代表电梯正在上行，从最高层向下检索需求数组，将目标楼层更新为第一个需求和原本目标楼层中较大的，根据这个目标是上行还是下行重置state；
-	如果state为-1或-2，代表电梯正在下行，从最低层向上检索需求数组，将目标楼层更新为第一个需求和原本目标楼层中较小的，根据这个目标是上行还是下行重置state。

(2)	Elev 类中的upFloor() : 上行函数
```
1.	public void upFloor(){     
2.	        elevatorInsideView.showState.setText("▲");  
3.	        for (int i=0; i<dataConst.floorHigh+dataConst.floorSpace; i++){     
4.	            try{      Thread.sleep(40);    }                                      
5.	            catch (InterruptedException e){}      
6.	            elevator.setLocation(elevator.getLocation().x,elevator.getLocation().y-1);//电梯示意图上行一个单元  
7.	        }  
8.	          
9.	        floor++; //当前楼层上移一层  
10.	        elevatorInsideView.showFloor.setText(""+floor);  
11.	          
12.	        if (aimFloor==floor && //到达目标楼层  
13.	            state==2 &&          //当前上未来下  
14.	            !arrive[floor-1] &&  //无向上需求  
15.	            !Button.call[floor-1]){           
16.	            elevatorInsideView.floorButton[floor-1].setBackground(Color.white);  
17.	            arrive[2*dataConst.totalFloor-floor]=false;                   
18.	            Button.call[2*dataConst.totalFloor-floor]=false;           
19.	       Button.callButton[2*dataConst.totalFloor-floor].setBackground(Color.white);           
20.	             openButton();             
21.	        }  
22.	        //向上需求  
23.	        else if (arrive[floor-1] || Button.call[floor-1] ){  
24.	            elevatorInsideView.floorButton[floor-1].setBackground(Color.white);  
25.	            arrive[floor-1]=false;  
26.	            Button.call[floor-1]=false;           
27.	            Button.callButton[floor-1].setBackground(Color.white);            
28.	            openButton();    }            
29.	          }  
```

(3)	Button 类中的isNearest(Elev e, int floor) : 判断当前电梯是否响应请求
```
1.	//路程最短的电梯  
2.	    public static boolean isNearest(Elev ele, int floor)  
3.	    {  
4.	        for (int i=0; i<dataConst.totalElevator; i++)  
5.	        {  
6.	            if (dataConst.elevator[i].getElevatorState()==0   
7.	                && Math.abs(ele.getFloor()-floor)>  
8.	                   Math.abs(dataConst.elevator[i].getFloor()-floor))   
9.	                return false;  
10.	        }  
11.	        return true;  
12.	    }  
```
### 2. 界面实现
#### 2.1. 滚动界面的实现
为了保证程序可以适用于可变的电梯数及楼层数，将最后的显示界面放在可滚动的界面容器中。所以我们将电梯外按钮群、电梯内按钮群、电梯运动模拟按钮群都加入各自对应的JPanel中，再将JPanel放入滚动面板JScrollPane中，最后将滚动面板加入JFrame。实现滚动界面。代码摘要如下：
```
1.	JPanel container=new JPanel()  
2.	container.setPreferredSize(new Dimension(  
3.	                    dataConst.totalElevator*200+140,  
4.	                    (dataConst.floorHigh+dataConst.floorSpace)*dataConst.totalFloor+35+10  
5.	                ));  
6.	container.add(outsideView);  
7.	container.add(insideViewButton);  
8.	JScrollPane scrollPane = new JScrollPane(container);  
9.	scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);  
10.	scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);  
11.	//JFrame  
12.	this.setSize(frameWide,frameHigh);  
13.	this.getContentPane().add(scrollPane);  
14.	this.setVisible(true);  
15.	this.setResizable(true);  
16.	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
```


#### 2.2. 实现效果
(1)	当楼层少于20，电梯少于5时，页面随之改变大小

 

(2)	当楼层为20，电梯为5时，达到最大页面

 



(3)	当楼层大于20，或电梯大于5时，页面出现滚动条
 
 


## 三、改进
目前程序还存在着以下问题：

 1. 当楼层数小于8时，无法显示电梯信息的显示屏；
 
 2. 无法控制外部电梯按钮按完，才可以按动内部按钮；
 
 3. 电梯的调度算法优先调用了静止的电梯，但实际情况下这种方案并不可取。
