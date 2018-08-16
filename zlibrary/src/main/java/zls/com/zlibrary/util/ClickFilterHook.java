package zls.com.zlibrary.util;

import android.util.Log;
import android.view.View;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * ===============iflytek===============
 * <p>
 * <p>  说明 ： 通过AOP过滤所有的重复点击
 * <p>  作者 ： 陈振
 * <p>  时间 ： 2018/7/11 18:16
 * <p>  邮箱 ： zhenchen4@iflytek.com
 * <p>
 * ===============iflytek===============
 */

@Aspect
public class ClickFilterHook {
    private static Long sLastclick = 0L;
    private static final Long FILTER_TIMEM = 1000L;
    private static int sHashCode;

    @Around("execution(* android.view.View.OnClickListener.onClick(..))")
    public void clickFilterHook(ProceedingJoinPoint joinPoint) {
        //获取被点击view的唯一标示hashcode
        Object[] args = joinPoint.getArgs();
        View view = (View) args[0];
        int hashCode = view.hashCode();

        if (sHashCode == hashCode && System.currentTimeMillis() - sLastclick <= FILTER_TIMEM) {
            Log.e("ClickFilterHook", "同一个view重复点击,已过滤");
        } else {
            //正常执行
            sLastclick = System.currentTimeMillis();
            sHashCode = hashCode;
            try {
                joinPoint.proceed();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

}
