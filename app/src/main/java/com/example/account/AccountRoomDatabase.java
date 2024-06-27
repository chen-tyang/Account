package com.example.account;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
// 指定这是一个 Room 数据库，并且包含一个名为 Account 的实体，版本号为 1，不导出数据库架构信息
@Database(entities = {Account.class}, version = 1, exportSchema = false)
public abstract class AccountRoomDatabase extends RoomDatabase {

    // 抽象方法，用于获取 AccountDao 实例
    public abstract AccountDao accountDao();

    // 持有 AccountRoomDatabase 的单例实例
    private static AccountRoomDatabase INSTANCE;

    /**
     * 获取数据库实例的静态方法
     * 使用双重检查锁定确保线程安全
     *
     * @param context 应用程序上下文
     * @return AccountRoomDatabase 的单例实例
     */
    static AccountRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) { // 如果 INSTANCE 为空，进入同步块检查
            synchronized (AccountRoomDatabase.class) { // 同步块，确保线程安全
                if (INSTANCE == null) { // 再次检查 INSTANCE 是否为空
                    INSTANCE = // 创建 AccountRoomDatabase 实例
                            Room.databaseBuilder(context.getApplicationContext(),
                                            AccountRoomDatabase.class, "account_database") // 指定数据库类和数据库名称
                                    .fallbackToDestructiveMigration() // 如果没有提供迁移策略，销毁旧数据库并创建新数据库
                                    .build(); // 构建 Room 数据库实例
                }
            }
        }
        return INSTANCE; // 返回数据库实例
    }
}
