package srt.core;

import srt.exception.*;

public class SrtTime {
	int hour;
	int minute;
	int second;
	int msecond;

	public static final int MAX_HOUR = 99;
	public static final int MAX_MINUTE = 59;
	public static final int MAX_SECOND = 59;
	public static final int MAX_MSECOND = 999;

	public static final int MIN_HOUR = -99;
	public static final int MIN_MINUTE = -59;
	public static final int MIN_SECOND = -59;
	public static final int MIN_MSECOND = -999;

	public static final int CONST_HOUR_CONVERT_MSECOND = 3600000;
	public static final int CONST_MINUTE_CONVERT_MSECOND = 60000;
	public static final int CONST_SECOND_CONVERT_MSECOND = 1000;
	public static final int CONST_MINUTE_CONVERT_SECOND = 60;
	public static final int CONST_HOUR_CONVERT_MINUTE = 60;


	public SrtTime() {

	}

	SrtTime(int hour,int minute,int second,int msecond) {
		this.hour=hour;
		this.minute=minute;
		this.second=second;
		this.msecond=msecond;
	}

	public String toString(){
		String str = "";
		str+=String.format("%02d",this.hour);
		str+=":";
		str+=String.format("%02d",this.minute);
		str+=":";
		str+=String.format("%02d",this.second);
		str+=",";
		str+=String.format("%03d",this.msecond);
		return str;
	}

	public int asMsecondInt(){
		int ms = msecond;
		ms+=(hour*CONST_HOUR_CONVERT_MSECOND);
		ms+=(minute*CONST_MINUTE_CONVERT_MSECOND);
		ms+=(second*CONST_SECOND_CONVERT_MSECOND);
		return ms;
	}
	// assume a Hour,b Minute,c Second,d Msecond,z const=1000
	// and x ms in total
	// then x=a*z*60^2+b*z*60+c*z+d
	// x/z = a*z*60+b*60+c
	// x/(z*60) = a*z+b
	// x/(z*60*60) = a*z/60
	// x
	//有问题，设置不上
	public void setMsecondInt(int msecond) throws SrtTimeOutOfBoundaryException {
		int totalSecond = (msecond - msecond%CONST_SECOND_CONVERT_MSECOND)/CONST_SECOND_CONVERT_MSECOND;
		int totalMinute = (totalSecond-totalSecond%CONST_MINUTE_CONVERT_SECOND)/CONST_MINUTE_CONVERT_SECOND;
		int totalHour = (totalMinute-totalMinute%CONST_HOUR_CONVERT_MINUTE)/CONST_HOUR_CONVERT_MINUTE;

		this.setMsecond(msecond%CONST_SECOND_CONVERT_MSECOND);
		this.setSecond((totalSecond%CONST_MINUTE_CONVERT_SECOND));
		this.setMinute((totalMinute%CONST_HOUR_CONVERT_MINUTE));
		this.setHour(totalHour);
	}

	public int getHour() {
		return hour;
	}

	public void setHour(int hour) throws SrtTimeHourOutOfBoundaryException {
		if(hour> MAX_HOUR || hour < MIN_HOUR){
			throw new SrtTimeHourOutOfBoundaryException();
		}
		this.hour=hour;
	}

	public int getMinute() {
		return minute;
	}

	public void setMinute(int minute) throws SrtTimeMinuteOutOfBoundaryException {
		if(minute> MAX_MINUTE || minute < MIN_MINUTE){
			throw new SrtTimeMinuteOutOfBoundaryException();
		}
		this.minute = minute;
	}

	public int getSecond() {
		return second;
	}

	public void setSecond(int second) throws SrtTimeSecondOutOfBoundaryException {
		if(second> MAX_SECOND || second < MIN_SECOND){
			throw new SrtTimeSecondOutOfBoundaryException();
		}
		this.second = second;
	}

	public int getMsecond() {
		return msecond;
	}

	public void setMsecond(int msecond) throws SrtTimeMsecondOutOfBoundaryException {
		if(msecond > MAX_MSECOND || msecond < MIN_MSECOND){
			throw new SrtTimeMsecondOutOfBoundaryException();
		}
		this.msecond = msecond;
	}
}


