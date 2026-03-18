<!--
	Natives Interface
	
	Copyright 2011-2012 IS2T. All rights reserved.
	Modification and distribution is permitted under certain conditions.
	IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->
<nativesInterface>
	
	
	<nativesPool name="com.is2t.natives.sp.ShieldedPlugNativesPool">
		
		<native name="com.is2t.natives.sp.NShieldedPlug.getDatabasePtr(int)int"/>
		<native name="com.is2t.natives.sp.NShieldedPlug.getSize(int)int"/>
		<native name="com.is2t.natives.sp.NShieldedPlug.getID(int)int"/>
		<native name="com.is2t.natives.sp.NShieldedPlug.getIDs(int,int[])void"/>
		<native name="com.is2t.natives.sp.NShieldedPlug.exists(int,int)int"/>
		<native name="com.is2t.natives.sp.NShieldedPlug.getLength(int,int)int"/>
		<native name="com.is2t.natives.sp.NShieldedPlug.getMaxTasks(int,int)int"/>
		<native name="com.is2t.natives.sp.NShieldedPlug.getDataState(int,int)int"/>
		<native name="com.is2t.natives.sp.NShieldedPlug.reset(int,int)int"/>
		<native name="com.is2t.natives.sp.NShieldedPlug.read(int,int,byte[],int,boolean)int"/>
		<native name="com.is2t.natives.sp.NShieldedPlug.write(int,int,byte[],int,boolean)int"/>
		<native name="com.is2t.natives.sp.NShieldedPlug.waitOne(int,int)int"/>
		<native name="com.is2t.natives.sp.NShieldedPlug.waitSeveral(int,int[])int"/>
		<native name="com.is2t.natives.sp.NShieldedPlug.removeWaitFlag(int,int)int"/>
		<native name="com.is2t.natives.sp.NShieldedPlug.removeWaitFlags(int,int[],boolean[])int"/>
	
	</nativesPool>
	
</nativesInterface>