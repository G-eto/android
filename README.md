# 日记本
## 背景介绍
1. 2019.07应生产实习课程要求，选择安卓开发组，以日记本为项目展开作业;
2. 此前已学Java基本知识，未接触安卓开发;
3. 在实践中学习，第一周重现教程，熟悉各文件作用，学会分析并解决bug；
4. 第二、三周项目原型设计与编码；
## 项目介绍
实现一个app，使之具备日记本的基本功能，写日记，读日记，改日记，删日记。
使用sqlite存储数据，markdownview展示日记文本
## 数据库
一张日记表，基本字段有：  
![](https://github.com/G-eto/android/blob/master/picture/db.png)

## 基本功能
### 1. 日记本：
1. 日记列表
2. 模糊搜索
3. 日记删除
### 2. 看日记：
1. 日记详情
2. 日记删除/复制
3. 左右滑动查看
### 3. 写日记：
1. 日记添加/修改
2. 日记信息录入

## 截图
![](https://github.com/G-eto/android/blob/master/picture/%E5%88%86%E6%9E%90.jpg)
![](https://github.com/G-eto/android/blob/master/picture/%E6%9F%A5%E7%9C%8B.jpg)
![](https://github.com/G-eto/android/blob/master/picture/%E7%BC%96%E8%BE%91.jpg)
![](https://github.com/G-eto/android/blob/master/picture/%E9%A6%96%E9%A1%B5.jpg)
## 总结
在这三周时间里，只能说大概了解了安卓开发流程与使用到的方面，对其中的细节以及其他未接触的地方还需要阅读资料与实践。


--- 
--- 
# New Target
---
## UI
## Picture
## Analysis




# android
标签（空格分隔）：安卓项目 日记本
---

## 需求分析
### 待解决的问题
#### 数据库
##### 待添加数据：
1. 地理位置
2. 一句话总结
3. 心情指数
4. 更新时间
5. ...

#### 首页
1. 菜单栏添加日历选择
2. 菜单设置按钮
3. Item最大高度
4. ...

#### 浏览页
1. 参考“一本日记”格式
2. 菜单栏按钮优化
3. 对准位置显示正确数据
4. 左右翻页
5. ...

#### 编辑页
1. 优化菜单及按钮
2. 编辑过长时不自动隐藏，光标到最下边
3. 参考一本日记格式
4. ...

---
# 功能 about DB
## 主页
1. 删除
2. getAll（）& count
3. 
## 查看（have id）
1. 删除
2. getNote（）
3.
## 编辑（have id [-1 || id > -1]）
1. create() all data(include id, timestamp)
2. update() note, number, kind, weather, newTime, location, inshort, mood, state
3. 

# Now class
#### class DBhelper{
1. long insertNote(String note, String kind, String weather, int wordnumber,
                           String location, String inshort, String state, int mood) 
2. Note getNote(int id)
3. public List<Note> getAllNotes()
4. public int getNotesCount()
5. public int updateNote(Note note)
6. public void deleteNote(Note note)
7. public List<Note> findNoteByDate(String date)


}
