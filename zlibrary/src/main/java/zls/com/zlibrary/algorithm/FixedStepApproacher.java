package zls.com.zlibrary.algorithm;

/**
 * Created by oop on 2018/8/16.
 */

public class FixedStepApproacher {

    public interface Callback{
        //使用给的参数 curParam 得到的值
        int getValueRelatedToCurParam(int curParam);
    }

    /*
        以固定步伐调整参数，使得参数对应的值最接近目标值，返回此条件下的参数
        aimValue：目标值
    */
    public static int findMostAppropriateParam(int aimValue, Callback callback){
        final int maxStep = 10;
        final int minStep = 1;

        int curStep = maxStep;
        int curParam = 0;
        int curValue = 0;

        int minGap = aimValue;
        int minGapReachedTime = 0;//在反复达到最小 gap 时跳出循环，防止死循环

        while(Math.abs(aimValue - curValue) > 1 && minGapReachedTime <= 2){
            if(aimValue > curValue){
                curParam += curStep;
            }else {
                curParam -= curStep;
            }
            curValue = callback.getValueRelatedToCurParam(curParam);
            if(curValue > aimValue){
                curStep = minStep;
            }

            int gap = Math.abs(curValue -aimValue);
            if(gap < minGap){
                minGap = gap;
                minGapReachedTime = 0;
            }else if(gap == minGap){
                minGapReachedTime ++;
            }
        }

        return curParam;
    }

}
