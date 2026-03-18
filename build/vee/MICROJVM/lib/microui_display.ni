<!--
	FNI
	
	Copyright 2011-2023 MicroEJ Corp. All rights reserved.
	This library is provided in source code for use, modification and test, subject to license terms.
	Any modification of the source code will break MicroEJ Corp. warranties on the whole library.
-->
<nativesInterface>

	<nativesPool name="com.is2t.microui.MicroUIDisplayNativesPool">
		
		<native name="com.is2t.microbsp.microui.natives.NSystemDisplay.getNbBitsPerPixel()int"/>
		<native name="com.is2t.microbsp.microui.natives.NSystemDisplay.isColor()boolean"/>
		<native name="com.is2t.microbsp.microui.natives.NSystemDisplay.getNumberOfColors()int"/>
		<native name="com.is2t.microbsp.microui.natives.NSystemDisplay.isDoubleBuffered()boolean"/>
		<native name="com.is2t.microbsp.microui.natives.NSystemDisplay.hasBacklight()boolean"/>
		
		<native name="com.is2t.microbsp.microui.natives.NSystemDisplay.setContrast(int)void"/>
		<native name="com.is2t.microbsp.microui.natives.NSystemDisplay.getContrast()int"/>
		<native name="com.is2t.microbsp.microui.natives.NSystemDisplay.setBacklight(int)void"/>
		<native name="com.is2t.microbsp.microui.natives.NSystemDisplay.getBacklight()int"/>
		
		<native name="com.is2t.microbsp.microui.natives.NSystemDisplay.openOffScreen(int,int,byte[],byte)int"/>
		<native name="com.is2t.microbsp.microui.natives.NSystemDisplay.linkImage(byte[],int,boolean)void"/>
		
		<native name="com.is2t.microbsp.microui.natives.NSystemDisplay.getDisplayColor(int)int"/>
		
		<native name="com.is2t.microbsp.microui.natives.NSystemDisplay.loadImageFromPath(byte[],int,boolean,boolean,boolean,int,byte[])int"/>
		<native name="com.is2t.microbsp.microui.natives.NSystemDisplay.loadImageFromBytes(byte[],int,int,int,byte[])int"/>
		<native name="com.is2t.microbsp.microui.natives.NSystemDisplay.loadImageFromMIS(com.is2t.vm.support.io.MemoryInputStream,int,int,byte[])int"/>
		<native name="com.is2t.microbsp.microui.natives.NSystemDisplay.isTransparent(byte[])boolean"/>
		
		<native name="com.is2t.microbsp.microui.natives.NSystemDisplay.addInternalFont(byte[],int,byte[])int"/>
		<native name="com.is2t.microbsp.microui.natives.NSystemDisplay.getFontIdentifiers(byte[],int[])int"/>
		
	</nativesPool>

	<nativeRequirements name="com.is2t.microbsp.microui.natives.NSystemDisplay.getFontIdentifiers(byte[],int[])int">
		<type name="java.lang.NullPointerException"/>
	</nativeRequirements>
	
</nativesInterface>
