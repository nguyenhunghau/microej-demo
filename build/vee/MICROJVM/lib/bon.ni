<!--
	Natives Interface
	
	Copyright 2011-2021 IS2T. All rights reserved.
	Modification and distribution is permitted under certain conditions.
	IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
-->
<nativesInterface>

	<nativesPool name="ist.mowana.vm.GenericNativesPool">
		<native name="com.is2t.bon.ImmutablesNatives.fillImmutableKeys(java.lang.String[])void"/>
		<native name="com.is2t.bon.ImmutablesNatives.get(java.lang.String)java.lang.Object"/>
		<native name="com.is2t.bon.ImmutablesNatives.getNbImmutableKeys()int"/>
		<native name="com.is2t.bon.ImmutablesNatives.isImmutable(java.lang.Object)boolean"/>
		<native name="com.is2t.bon.ImmutablesNatives.initialImmutableMemory()long"/>
		<native name="ej.bon.Immortals.isImmortal(java.lang.Object)boolean"/>
		<native name="ej.bon.Immortals.totalMemory()long"/>
		<native name="ej.bon.Immortals.freeMemory()long"/>
		<native name="ej.bon.Immortals.setImmortal(java.lang.Object)java.lang.Object"/>
		<native name="ej.bon.Immortals.switchAllocationIntoImmortals(boolean)void"/>
	</nativesPool>
	
	<nativesPool name="com.is2t.bon.Bon1xNativesPool">
		<native name="ej.bon.Util.throwExceptionInThread(java.lang.RuntimeException,java.lang.Thread,boolean)void"/>
		<native name="ej.bon.Util.isInInitialization()boolean"/>
		<native name="ej.bon.CurrentTime.get(boolean)long"/>
		<native name="ej.bon.CurrentTime.getNanos()long"/>
		<native name="ej.bon.CurrentTime.set(long)void"/>
		<native name="ej.bon.XMath.acos(double)double"/>
		<native name="ej.bon.XMath.asin(double)double"/>
		<native name="ej.bon.XMath.atan(double)double"/>
		<native name="ej.bon.XMath.log(double)double"/>
		<native name="ej.bon.XMath.exp(double)double"/>
		<native name="ej.bon.XMath.pow(double,double)double"/>
		<native name="ej.bon.ByteArray.isBigEndian()boolean"/>
		<native name="ej.bon.ByteArray.clear(byte[],int,int)void"/>
		<native name="ej.bon.ByteArray.set(byte[],byte,int,int)void"/>
		<native name="ej.bon.ResourceBuffer.readVarSInt()int"/>
		<native name="ej.bon.ResourceBuffer.readVarUInt()int"/>
		<native name="ej.bon.ResourceBuffer.readVarLong()long"/>
		<!--
		<native name="ej.bon.ResourceBuffer.readBoolean()boolean"/>
		<native name="ej.bon.ResourceBuffer.readFloat()float"/>
		<native name="ej.bon.ResourceBuffer.readDouble()double"/>
		-->
		<native name="ej.bon.ResourceBuffer.readUnsignedByte()int"/>
		<native name="ej.bon.ResourceBuffer.readSignedByte()int"/>
		<native name="ej.bon.ResourceBuffer.readString()java.lang.String"/>
		<native name="ej.bon.ResourceBuffer.platformReadUInt16()int"/>
		<native name="ej.bon.ResourceBuffer.platformReadUInt32()int"/>
		<native name="ej.bon.ResourceBuffer.platformReadInt16()int"/>
		<native name="ej.bon.ResourceBuffer.platformReadInt32()int"/>
		<native name="ej.bon.ResourceBuffer.open(byte[],int)boolean"/>
		<native name="ej.bon.ResourceBuffer.close()void"/>
		
		<native name="ej.bon.Util.newArray(java.lang.Class,int)java.lang.Object[]"/>
		
		<!-- 
			Declare BON Constants natives for link purpose only.
			(theses native are never called because they are replaced by SOAR)
		-->
		<native name="ej.bon.Constants.getString(java.lang.String)java.lang.String"/>
		<native name="ej.bon.Constants.getBoolean(java.lang.String)boolean"/>
		<native name="ej.bon.Constants.getByte(java.lang.String)byte"/>
		<native name="ej.bon.Constants.getChar(java.lang.String)char"/>
		<native name="ej.bon.Constants.getClass(java.lang.String)java.lang.Class"/>
		<native name="ej.bon.Constants.getDouble(java.lang.String)double"/>
		<native name="ej.bon.Constants.getFloat(java.lang.String)float"/>
		<native name="ej.bon.Constants.getInt(java.lang.String)int"/>
		<native name="ej.bon.Constants.getLong(java.lang.String)long"/>
		<native name="ej.bon.Constants.getShort(java.lang.String)short"/>
	</nativesPool>
	
	<nativeRequirements name="ej.bon.ImmutablesBlock.initialize()void">
		<type name="ej.bon.ImmutablesError"/>
	</nativeRequirements>
	<nativeRequirements name="ej.bon.ImmutablesBlock.erase(ej.bon.ImmutablesBlock[])void">
		<type name="ej.bon.ImmutablesError"/>
	</nativeRequirements>
	<nativeRequirements name="ej.bon.ImmutablesTable.writeBuffer(int,int,byte[])void">
		<type name="ej.bon.ImmutablesError"/>
	</nativeRequirements>

	<nativeRequirements name="ej.bon.ResourceBuffer.readVarSInt()int">
		<type name="java.io.IOException"/>
		<type name="java.io.EOFException"/>
	</nativeRequirements>
	<nativeRequirements name="ej.bon.ResourceBuffer.readVarUInt()int">
		<type name="java.io.IOException"/>
		<type name="java.io.EOFException"/>
	</nativeRequirements>
	<nativeRequirements name="ej.bon.ResourceBuffer.readVarLong()long">
		<type name="java.io.IOException"/>
		<type name="java.io.EOFException"/>
	</nativeRequirements>
	<!--
	<nativeRequirements name="ej.bon.ResourceBuffer.readFloat()float">
		<type name="java.io.IOException"/>
		<type name="java.io.EOFException"/>
	</nativeRequirements>
	<nativeRequirements name="ej.bon.ResourceBuffer.readDouble()double">
		<type name="java.io.IOException"/>
		<type name="java.io.EOFException"/>
	</nativeRequirements>
	-->
	<nativeRequirements name="ej.bon.ResourceBuffer.readUnsignedByte()int">
		<type name="java.io.IOException"/>
		<type name="java.io.EOFException"/>
	</nativeRequirements>
	<nativeRequirements name="ej.bon.ResourceBuffer.readSignedByte()int">
		<type name="java.io.IOException"/>
		<type name="java.io.EOFException"/>
	</nativeRequirements>
	<nativeRequirements name="ej.bon.ResourceBuffer.readString()java.lang.String">
		<type name="java.io.IOException"/>
		<type name="java.io.EOFException"/>
	</nativeRequirements>
	<nativeRequirements name="ej.bon.ResourceBuffer.platformReadUInt16()int">
		<type name="java.io.IOException"/>
		<type name="java.io.EOFException"/>
	</nativeRequirements>
	<nativeRequirements name="ej.bon.ResourceBuffer.platformReadInt16()int">
		<type name="java.io.IOException"/>
		<type name="java.io.EOFException"/>
	</nativeRequirements>
	<nativeRequirements name="ej.bon.ResourceBuffer.platformReadInt32()int">
		<type name="java.io.IOException"/>
		<type name="java.io.EOFException"/>
	</nativeRequirements>
	
</nativesInterface>