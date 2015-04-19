package com.noahhuppert.reflect.core.settings;
import com.raizlabs.android.dbflow.runtime.transaction.TransactionListenerAdapter;
import com.raizlabs.android.dbflow.sql.builder.Condition;
import com.raizlabs.android.dbflow.sql.language.Select;

public class UserSettings {
    public static final String VALUE_TRUE = "TRUE";
    public static final String VALUE_FALSE = "FALSE";

    public static final String SETTING_DEFAULT_COUNTRY_CODE = "SETTING_DEFAULT_COUNTRY_CODE";

    /* Getters */
    public static void GetSetting(String key, final UserSettingRetrievedListener userSettingRetrievedListener){
        new Select().from(UserSetting.class)
                .where(Condition.column(UserSetting$Table.KEY).eq(key))
                .transactSingleModel(new TransactionListenerAdapter<UserSetting>() {
                    @Override
                    public void onResultReceived(UserSetting result) {
                        userSettingRetrievedListener.OnResult(result);
                    }
                });
    }

    public static UserSetting GetSettingSync(String key){
        return new Select().from(UserSetting.class)
                .where(Condition.column(UserSetting$Table.KEY).eq(key))
                .querySingle();
    }

    public static boolean ValueAsBoolean(String value){
        return value.equals(VALUE_TRUE);
    }

    /* Actions */
    public static void SetDefaults(){
        InsertIfNotExists(new UserSetting(SETTING_DEFAULT_COUNTRY_CODE, "us"));
    }

    private static void InsertIfNotExists(UserSetting userSetting){
        if(!userSetting.exists()){
            userSetting.insert(false);
        }
    }
}
