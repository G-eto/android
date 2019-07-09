# android
标签（空格分隔）：安卓项目 日记本
---

##需求分析
###待解决的问题
####数据库
#####待添加数据：
1. 地理位置
2. 一句话总结
3. 心情指数
4.更新时间
5. ...

####首页
1. 菜单栏添加日历选择
2. 菜单设置按钮
3. Item最大高度
4. ...

####浏览页
1. 参考“一本日记”格式
2. 菜单栏按钮优化
3. 对准位置显示正确数据
4. 左右翻页
5. ...

####编辑页
1. 优化菜单及按钮
2. 编辑过长时不自动隐藏，光标到最下边
3. 参考一本日记格式
4. ...

---
#功能 about DB
##主页
1. 删除
2. getAll（）& count
3. 
##查看（have id）
1. 删除
2. getNote（）
3.
##编辑（have id [-1 || id > -1]）
1. create() all data(include id, timestamp)
2. update() note, number, kind, weather, newTime, location, inshort, mood, state
3. 

#Now class
####class DBhelper{
1. long insertNote(String note, String kind, String weather, int wordnumber,
                           String location, String inshort, String state, int mood) 
2. Note getNote(int id)
3. public List<Note> getAllNotes()
4. public int getNotesCount()
5. public int updateNote(Note note)
6. public void deleteNote(Note note)
7. public List<Note> findNoteByDate(String date)


}
