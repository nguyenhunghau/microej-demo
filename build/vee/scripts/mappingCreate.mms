# MMS
#
# Copyright 2009-2023 IS2T. All rights reserved.
# Modification and distribution is permitted under certain conditions.
# IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.


# This script only declares the graphs. The declared graph may contain more symbol than
# wanted (eg. VMCore includes VMAllocation symbols). Only union of graphs are allowed here.
# Unwanted symbols will be removed in computeGraphsXXX scripts.
#
# This script declares only common graphs. 
# Platform specific graphs should be added from specific createGraphsXXX script.
# Platform specific symbols should be added in common graph from specific createGraphsXXX script.
#
# When declaring a new graph, add all specific symbols (eg. com_is2t_display.*, ...) and also obfuscation prefix (eg. LIBDISPLAY.*)
#
# "Remaining" graph must be empty. All symbols and sections must be in graph. 

# ALL
createGraphNoRec All section=.*
createGraphNoRec $TotalSoar section=.soar.text.* section=.soar.rodata.* section=.soar.runtime.* section=.bss.soar.* section=.bss.microej.statics.* _java_.*

# All statics
createGraphNoRec $Label_Statics section=.bss.soar.BT.* section=.bss.soar.REF.*

# RODATA_APPLI (fonts+images+resources+immutables)
createGraphNoRec $Label_ApplicationFonts .*\.ejf_1raw.* .*\.ttf.*
createGraphNoRec $Label_ApplicationImages .*\.png.* .*\.jpg.* .*\.jpeg.* .*\.bmp.* .*\.gif.* .*\.xml_1raw.* .*\.svg_1raw.*
createGraphNoRec $Label_ApplicationResources section=.soar.rodata.resource.* _java_rodata_resource.*
createGraphNoRec $Label_ApplicationImmutables  section=.soar.rodata.immutable.* _java_rodata_immutable.* _java_immutables_start _java_immutable_Keys _java_immutable_Values _java_Object_OutOfMemory

# Extract global .soar.runtime table  
createGraphNoRec $Label_JavaRuntime section=_ni_stackgroup_ESR_.* _java_sharedarray_start _java_stackgroup_table

# Class names
createGraphNoRec $Label_ClassNames _java_names_data_start section=.soar.rodata.classnames section=.soar.rodata.packagenames _java_class_names_start

# Runtime Tables
createGraphNoRec $Label_RuntimeTables _java_ClinitMethod _java_clinits_start _java_T.*_imtable.* _java_T.*_vtable _java_T.*_htable _java_T.*_itable _java_T.*_ihtable

# All types
createGraphNoRec $Label_Types section=.soar.runtime.types _java_header_types_start

# Java strings
createGraphNoRec $Label_ApplicationStrings _java_sharedarray_start _java_internStrings_start _java_properties_Keys _java_properties_Values section=.soar.rodata.internSymbol

# Foundation LIBS
createGraphNoRec $JavaEDC _java_[A]*[LP]com_is2t_cldc_support_ObjectGraphBrowser.* _java_[A]*[LP]com_is2t_cldc_support_ObjectSet.* _java_[A]*[LP]com_is2t_tools_ArrayTools.* _java_[A]*[LP]com_is2t_tools_BitFieldHelper.* _java_[A]*[LP]com_is2t_tools_GenericTools.* _java_[A]*[LP]com_is2t_tools_GeometryTools.* _java_[A]*[LP]com_is2t_tools_Rectangle.* _java_[A]*[LP]com_is2t_tools_ToolsErrorMessage.* _java_[A]*[LP]com_is2t_vm_support_CalibrationConstants.* _java_[A]*[LP]com_is2t_vm_support_DefaultTimeZoneImpl.* _java_[A]*[LP]com_is2t_vm_support_GMTTimeZone.* _java_[A]*[LP]com_is2t_vm_support_ImmutableTimeZoneImpl.* _java_[A]*[LP]com_is2t_vm_support_InternalLimitsError.* _java_[A]*[LP]com_is2t_vm_support_ResourceLoader.* _java_[A]*[LP]com_is2t_vm_support_TimeZoneComposedEntry.* _java_[A]*[LP]com_is2t_vm_support_TimeZoneEntry.* _java_[A]*[LP]com_is2t_vm_support_TimeZoneErrorMessages.* _java_[A]*[LP]com_is2t_vm_support_TimeZoneImpl.* _java_[A]*[LP]com_is2t_vm_support_err_EDCErrorMessages.* _java_[A]*[LP]com_is2t_vm_support_io_DebugOutputStream.* _java_[A]*[LP]com_is2t_vm_support_io_MemoryInputStream.* _java_[A]*[LP]com_is2t_vm_support_io_MemoryOutputStream.* _java_[A]*[LP]com_is2t_vm_support_io_NullOutputStream.* _java_[A]*[LP]com_is2t_vm_support_lang_SupportNumber.* _java_[A]*[LP]com_is2t_vm_support_lang_Systools.* _java_[A]*[LP]com_is2t_vm_support_util_EncUS_1ASCII.* _java_[A]*[LP]com_is2t_vm_support_util_EncUTF_18.* _java_[A]*[LP]com_is2t_vm_support_util_EncodingConversion.* _java_[A]*[LP]com_is2t_vm_support_util_GregorianCalendar.* _java_[A]*[LP]ej_error_ErrorMessages.* _java_[A]*[LP]ej_error_Message.* _java_[A]*[LP]java_io_ByteArrayInputStream.* _java_[A]*[LP]java_io_ByteArrayOutputStream.* _java_[A]*[LP]java_io_Closeable.* _java_[A]*[LP]java_io_DataInput.* _java_[A]*[LP]java_io_DataInputStream.* _java_[A]*[LP]java_io_DataOutput.* _java_[A]*[LP]java_io_DataOutputStream.* _java_[A]*[LP]java_io_EOFException.* _java_[A]*[LP]java_io_FilterInputStream.* _java_[A]*[LP]java_io_FilterOutputStream.* _java_[A]*[LP]java_io_Flushable.* _java_[A]*[LP]java_io_IOException.* _java_[A]*[LP]java_io_InputStream.* _java_[A]*[LP]java_io_InputStreamReader.* _java_[A]*[LP]java_io_InterruptedIOException.* _java_[A]*[LP]java_io_OutputStream.* _java_[A]*[LP]java_io_OutputStreamWriter.* _java_[A]*[LP]java_io_PrintStream.* _java_[A]*[LP]java_io_Reader.* _java_[A]*[LP]java_io_Serializable.* _java_[A]*[LP]java_io_UTFDataFormatException.* _java_[A]*[LP]java_io_UnsupportedEncodingException.* _java_[A]*[LP]java_io_Writer.* _java_[A]*[LP]java_lang_AbstractMethodError.* _java_[A]*[LP]java_lang_AbstractStringBuilder.* _java_[A]*[LP]java_lang_Appendable.* _java_[A]*[LP]java_lang_ArithmeticException.* _java_[A]*[LP]java_lang_ArrayIndexOutOfBoundsException.* _java_[A]*[LP]java_lang_ArrayStoreException.* _java_[A]*[LP]java_lang_AssertionError.* _java_[A]*[LP]java_lang_AutoCloseable.* _java_[A]*[LP]java_lang_Boolean.* _java_[A]*[LP]java_lang_Byte.* _java_[A]*[LP]java_lang_CharSequence.* _java_[A]*[LP]java_lang_Character.* _java_[A]*[LP]java_lang_Class.* _java_[A]*[LP]java_lang_ClassCastException.* _java_[A]*[LP]java_lang_ClassCircularityError.* _java_[A]*[LP]java_lang_ClassFormatError.* _java_[A]*[LP]java_lang_ClassNotFoundException.* _java_[A]*[LP]java_lang_CloneNotSupportedException.* _java_[A]*[LP]java_lang_Cloneable.* _java_[A]*[LP]java_lang_Comparable.* _java_[A]*[LP]java_lang_Deprecated.* _java_[A]*[LP]java_lang_Double.* _java_[A]*[LP]java_lang_Enum.* _java_[A]*[LP]java_lang_Error.* _java_[A]*[LP]java_lang_Exception.* _java_[A]*[LP]java_lang_ExceptionInInitializerError.* _java_[A]*[LP]java_lang_Float.* _java_[A]*[LP]java_lang_IllegalAccessError.* _java_[A]*[LP]java_lang_IllegalAccessException.* _java_[A]*[LP]java_lang_IllegalArgumentException.* _java_[A]*[LP]java_lang_IllegalMonitorStateException.* _java_[A]*[LP]java_lang_IllegalStateException.* _java_[A]*[LP]java_lang_IllegalThreadStateException.* _java_[A]*[LP]java_lang_IncompatibleClassChangeError.* _java_[A]*[LP]java_lang_IndexOutOfBoundsException.* _java_[A]*[LP]java_lang_InstantiationError.* _java_[A]*[LP]java_lang_InstantiationException.* _java_[A]*[LP]java_lang_Integer.* _java_[A]*[LP]java_lang_InternalError.* _java_[A]*[LP]java_lang_InterruptedException.* _java_[A]*[LP]java_lang_Iterable.* _java_[A]*[LP]java_lang_LinkageError.* _java_[A]*[LP]java_lang_Long.* _java_[A]*[LP]java_lang_MainThread.* _java_[A]*[LP]java_lang_Math.* _java_[A]*[LP]java_lang_NegativeArraySizeException.* _java_[A]*[LP]java_lang_NoClassDefFoundError.* _java_[A]*[LP]java_lang_NoSuchFieldError.* _java_[A]*[LP]java_lang_NoSuchFieldException.* _java_[A]*[LP]java_lang_NoSuchMethodError.* _java_[A]*[LP]java_lang_NoSuchMethodException.* _java_[A]*[LP]java_lang_NullPointerException.* _java_[A]*[LP]java_lang_Number.* _java_[A]*[LP]java_lang_NumberFormatException.* _java_[A]*[LP]java_lang_Object.* _java_[A]*[LP]java_lang_OutOfMemoryError.* _java_[A]*[LP]java_lang_Override.* _java_[A]*[LP]java_lang_Package.* _java_[A]*[LP]java_lang_Readable.* _java_[A]*[LP]java_lang_ReflectiveOperationException.* _java_[A]*[LP]java_lang_Runnable.* _java_[A]*[LP]java_lang_Runtime.* _java_[A]*[LP]java_lang_RuntimeException.* _java_[A]*[LP]java_lang_RuntimePermission.* _java_[A]*[LP]java_lang_SafeVarargs.* _java_[A]*[LP]java_lang_SecurityException.* _java_[A]*[LP]java_lang_SecurityManager.* _java_[A]*[LP]java_lang_Short.* _java_[A]*[LP]java_lang_StackOverflowError.* _java_[A]*[LP]java_lang_StackTraceElement.* _java_[A]*[LP]java_lang_String$CaseInsensitiveOrder.* _java_[A]*[LP]java_lang_String.* _java_[A]*[LP]java_lang_StringBuffer.* _java_[A]*[LP]java_lang_StringBuilder.* _java_[A]*[LP]java_lang_StringIndexOutOfBoundsException.* _java_[A]*[LP]java_lang_SuppressWarnings.* _java_[A]*[LP]java_lang_System.* _java_[A]*[LP]java_lang_Thread$State.* _java_[A]*[LP]java_lang_Thread$UncaughtExceptionHandler.* _java_[A]*[LP]java_lang_Thread.* _java_[A]*[LP]java_lang_Throwable.* _java_[A]*[LP]java_lang_UnknownError.* _java_[A]*[LP]java_lang_UnsatisfiedLinkError.* _java_[A]*[LP]java_lang_UnsupportedClassVersionError.* _java_[A]*[LP]java_lang_UnsupportedOperationException.* _java_[A]*[LP]java_lang_VerifyError.* _java_[A]*[LP]java_lang_VirtualMachineError.* _java_[A]*[LP]java_lang_annotation_Annotation.* _java_[A]*[LP]java_lang_annotation_Documented.* _java_[A]*[LP]java_lang_annotation_ElementType.* _java_[A]*[LP]java_lang_annotation_Inherited.* _java_[A]*[LP]java_lang_annotation_Retention.* _java_[A]*[LP]java_lang_annotation_RetentionPolicy.* _java_[A]*[LP]java_lang_annotation_Target.* _java_[A]*[LP]java_lang_ref_Reference.* _java_[A]*[LP]java_lang_ref_ReferenceQueue.* _java_[A]*[LP]java_lang_ref_SoftReference.* _java_[A]*[LP]java_lang_ref_WeakReference.* _java_[A]*[LP]java_lang_reflect_AnnotatedElement.* _java_[A]*[LP]java_lang_reflect_GenericDeclaration.* _java_[A]*[LP]java_lang_reflect_Type.* _java_[A]*[LP]java_security_BasicPermission.* _java_[A]*[LP]java_security_Guard.* _java_[A]*[LP]java_security_Permission.* _java_[A]*[LP]java_util_AbstractArrayList$AbstractArrayListIterator.* _java_[A]*[LP]java_util_AbstractArrayList$AbstractArrayListListIterator.* _java_[A]*[LP]java_util_AbstractArrayList.* _java_[A]*[LP]java_util_AbstractCollection.* _java_[A]*[LP]java_util_AbstractHashMap$AbstractHashMapCollection.* _java_[A]*[LP]java_util_AbstractHashMap$AbstractHashMapEntry.* _java_[A]*[LP]java_util_AbstractHashMap$AbstractHashMapEnumeration.* _java_[A]*[LP]java_util_AbstractHashMap$AbstractHashMapIterator.* _java_[A]*[LP]java_util_AbstractHashMap$AbstractHashMapSet.* _java_[A]*[LP]java_util_AbstractHashMap.* _java_[A]*[LP]java_util_AbstractList$InternalIterator.* _java_[A]*[LP]java_util_AbstractList$InternalListIterator.* _java_[A]*[LP]java_util_AbstractList.* _java_[A]*[LP]java_util_AbstractMap$1$1.* _java_[A]*[LP]java_util_AbstractMap$1.* _java_[A]*[LP]java_util_AbstractMap$2$1.* _java_[A]*[LP]java_util_AbstractMap$2.* _java_[A]*[LP]java_util_AbstractMap$AbstractMapEntry.* _java_[A]*[LP]java_util_AbstractMap$SimpleEntry.* _java_[A]*[LP]java_util_AbstractMap$SimpleImmutableEntry.* _java_[A]*[LP]java_util_AbstractMap.* _java_[A]*[LP]java_util_AbstractSet.* _java_[A]*[LP]java_util_ArrayList.* _java_[A]*[LP]java_util_Calendar.* _java_[A]*[LP]java_util_Collection.* _java_[A]*[LP]java_util_Comparator.* _java_[A]*[LP]java_util_ConcurrentModificationException.* _java_[A]*[LP]java_util_Date.* _java_[A]*[LP]java_util_Dictionary.* _java_[A]*[LP]java_util_EmptyStackException.* _java_[A]*[LP]java_util_Enumeration.* _java_[A]*[LP]java_util_EventListener.* _java_[A]*[LP]java_util_EventObject.* _java_[A]*[LP]java_util_HashMap$HashMapEntry.* _java_[A]*[LP]java_util_HashMap.* _java_[A]*[LP]java_util_Hashtable$HashMapForHashtable.* _java_[A]*[LP]java_util_Hashtable$HashtableEntry.* _java_[A]*[LP]java_util_Hashtable.* _java_[A]*[LP]java_util_Iterator.* _java_[A]*[LP]java_util_List.* _java_[A]*[LP]java_util_ListIterator.* _java_[A]*[LP]java_util_Map$Entry.* _java_[A]*[LP]java_util_Map.* _java_[A]*[LP]java_util_NoSuchElementException.* _java_[A]*[LP]java_util_Observable.* _java_[A]*[LP]java_util_Observer.* _java_[A]*[LP]java_util_PropertyPermission.* _java_[A]*[LP]java_util_Random.* _java_[A]*[LP]java_util_RandomAccess.* _java_[A]*[LP]java_util_RandomAccessList.* _java_[A]*[LP]java_util_Set.* _java_[A]*[LP]java_util_Stack.* _java_[A]*[LP]java_util_SubList$1.* _java_[A]*[LP]java_util_SubList.* _java_[A]*[LP]java_util_TimeZone.* _java_[A]*[LP]java_util_Timer$TimerThread.* _java_[A]*[LP]java_util_Timer.* _java_[A]*[LP]java_util_TimerTask.* _java_[A]*[LP]java_util_Vector$VectorEnumeration.* _java_[A]*[LP]java_util_Vector.* _java_[A]*[LP]java_util_WeakHashMap$NullKey.* _java_[A]*[LP]java_util_WeakHashMap$WeakHashMapEntry.* _java_[A]*[LP]java_util_WeakHashMap$WeakKey.* _java_[A]*[LP]java_util_WeakHashMap.* ON_java_lang_.* _java_Pjava _java_Pjava_util _java_Pjava_lang _java_Pjava_io _java_[A]*[LP]com_is2t_cldc.* _java_[A]*[LP]com_is2t_vm_support.* _java_[A]*[LP]ej_annotation.* _java_Ljava_io_package-info.* _java_Ljava_lang_package-info.* _java_Ljava_lang_ref_package-info.* _java_Ljava_lang_reflect_package-info.* _java_Ljava_util_package-info.* _java_Ljava_lang_annotation_package-info.*
createGraphNoRec $JavaBON _java_[LP]ej_bon.* _java_[LP]com_is2t_bon.* ON_ej_bon_.*
createGraphNoRec $JavaKF _java_[A]*[LP]ej_kf.* _java_[A]*[LP]com_is2t_kf.* _java_[A]*[LP]com_is2t_elflw.* _java_[A]*[LP]com_is2t_schedcontrol.* _java_Pej_lang _java_[A]*[LP]ej_lang_Resource.* _java_feature.* _java_kernel.* _java_signature.* _java_bss_feature.* section=.bss.microej.feature _java_rodata_feature.* _java_[A]*[LP]ej_lang_Watchdog.* _java_[A]*[LP]ej_lang_DefaultWatchdog.*
createGraphNoRec $JavaPAP _java_[A]*[LP]ej_pap.* _java_[A]*[LP]com_is2t_pap.* _java_[A]*[LP]com_is2t_platform.*
createGraphNoRec $JavaECOMSocket _java_[A]*[LP]ej_ecom_buffer_BufferPool.* _java_[A]*[LP]ej_ecom_buffer_PoolOfReusableResources.* _java_[A]*[LP]ej_ecom_connection_SocketErrorMessages.* _java_[A]*[LP]ej_ecom_connection_network_NetworkManager.* _java_[A]*[LP]ej_ecom_connection_network_NetworkUtils.* _java_[A]*[LP]ej_ecom_connection_serversocket_ConnectionFactory.* _java_[A]*[LP]ej_ecom_connection_socket_ConnectionFactory.* _java_[A]*[LP]ej_ecom_connection_socket_ServerSocketConnectionImpl.* _java_[A]*[LP]ej_ecom_connection_socket_ServerSocketConnectionNatives.* Java_ej_ecom_connection_socket_ServerSocketConnectionNatives.* _java_[A]*[LP]ej_ecom_connection_socket_SocketConnectionImpl.* _java_[A]*[LP]ej_ecom_connection_socket_SocketConnectionNatives.* Java_ej_ecom_connection_socket_SocketConnectionNatives.* _java_[A]*[LP]ej_ecom_connection_socket_SocketInputStreamImpl.* _java_[A]*[LP]ej_ecom_connection_socket_SocketInputStreamNatives.* Java_ej_ecom_connection_socket_SocketInputStreamNatives.* _java_[A]*[LP]ej_ecom_connection_socket_SocketOutputStreamImpl.* _java_[A]*[LP]ej_ecom_connection_socket_SocketOutputStreamNatives.* Java_ej_ecom_connection_socket_SocketOutputStreamNatives.* _java_[A]*[LP]ej_ecom_io_ServerSocketConnection.* _java_[A]*[LP]ej_ecom_io_SocketConnection.* _java_[A]*[LP]ej_ecom_sni_SNIReturn.* Java_ej_ecom_sni_SNIReturn.* 
createGraphNoRec $JavaECOMCOMM _java_[A]*[LP]ej_ecom_connection_comm_CommConnectionCreator$1.* _java_[A]*[LP]ej_ecom_connection_comm_CommConnectionCreator$2.* _java_[A]*[LP]ej_ecom_connection_comm_CommConnectionCreator.* _java_[A]*[LP]ej_ecom_connection_comm_CommConnectionImpl.* _java_[A]*[LP]ej_ecom_connection_comm_CommConnectionNatives.* Java_ej_ecom_connection_comm_CommConnectionNatives.* _java_[A]*[LP]ej_ecom_connection_comm_CommHardwareDescriptor.* _java_[A]*[LP]ej_ecom_connection_comm_CommInputStreamImpl.* _java_[A]*[LP]ej_ecom_connection_comm_CommInputStreamNatives.* Java_ej_ecom_connection_comm_CommInputStreamNatives.* _java_[A]*[LP]ej_ecom_connection_comm_CommOutputStreamImpl.* _java_[A]*[LP]ej_ecom_connection_comm_CommOutputStreamNatives.* Java_ej_ecom_connection_comm_CommOutputStreamNatives.* _java_[A]*[LP]ej_ecom_connection_comm_CommPortImpl.* _java_[A]*[LP]ej_ecom_connection_comm_ConnectionErrorMessages.* _java_[A]*[LP]ej_ecom_connection_comm_ConnectionFactory.* _java_[A]*[LP]ej_ecom_io_BitsInput.* _java_[A]*[LP]ej_ecom_io_BitsOutput.* _java_[A]*[LP]ej_ecom_io_CommConnection.* _java_[A]*[LP]ej_ecom_io_CommPort.* _java_[A]*[LP]ej_ecom_io_DataBitsInputStream.* _java_[A]*[LP]ej_ecom_io_DataBitsOutputStream.* 
createGraphNoRec $JavaECOM _java_[A]*[LP]com_is2t_ecom_ClassRecord.* _java_[A]*[LP]com_is2t_ecom_DeviceRegistry$ClassFilter.* _java_[A]*[LP]com_is2t_ecom_DeviceRegistry$DeviceIterator.* _java_[A]*[LP]com_is2t_ecom_DeviceRegistry$SubTypesFilter.* _java_[A]*[LP]com_is2t_ecom_DeviceRegistry$SuperTypesFilter.* _java_[A]*[LP]com_is2t_ecom_DeviceRegistry.* _java_[A]*[LP]com_is2t_ecom_DeviceRegistryFeature.* _java_[A]*[LP]com_is2t_ecom_DeviceRegistryKernel$SharedDeviceIterator.* _java_[A]*[LP]com_is2t_ecom_DeviceRegistryKernel$SharedInterfaceSuperTypesFilter.* _java_[A]*[LP]com_is2t_ecom_DeviceRegistryKernel.* _java_[A]*[LP]com_is2t_ecom_IDeviceRegistry.* _java_[A]*[LP]com_is2t_util_CompositeIterator.* _java_[A]*[LP]com_is2t_util_FixedLengthFIFOQueue.* _java_[A]*[LP]com_is2t_util_Pump.* _java_[A]*[LP]com_is2t_util_Queue.* _java_[A]*[LP]com_is2t_util_QueueFullException.* _java_[A]*[LP]ej_ecom_AbstractDevice.* _java_[A]*[LP]ej_ecom_Connectable.* _java_[A]*[LP]ej_ecom_Device.* _java_[A]*[LP]ej_ecom_DeviceManager$1.* _java_[A]*[LP]ej_ecom_DeviceManager$2.* _java_[A]*[LP]ej_ecom_DeviceManager.* _java_[A]*[LP]ej_ecom_DeviceManagerPermission.* _java_[A]*[LP]ej_ecom_HardwareDescriptor.* _java_[A]*[LP]ej_ecom_RegistrationEvent.* _java_[A]*[LP]ej_ecom_RegistrationListener.* _java_[A]*[LP]ej_ecom_connection_AbstractConnectionImpl.* _java_[A]*[LP]ej_ecom_connection_AbstractInputStream.* _java_[A]*[LP]ej_ecom_connection_AbstractOutputStream.* _java_[A]*[LP]ej_ecom_io_Connection.* _java_[A]*[LP]ej_ecom_io_ConnectionFactory.* _java_[A]*[LP]ej_ecom_io_ConnectionNotFoundException.* _java_[A]*[LP]ej_ecom_io_ConnectionPermission.* _java_[A]*[LP]ej_ecom_io_Connector.* _java_[A]*[LP]ej_ecom_io_ContentConnection.* _java_[A]*[LP]ej_ecom_io_Datagram.* _java_[A]*[LP]ej_ecom_io_DatagramConnection.* _java_[A]*[LP]ej_ecom_io_InputConnection.* _java_[A]*[LP]ej_ecom_io_OutputConnection.* _java_[A]*[LP]ej_ecom_io_StreamConnection.* _java_[A]*[LP]ej_ecom_io_StreamConnectionNotifier.* _java_[A]*[LP]ej_ecom_support_err_ConnectionErrorMessages.* 
createGraphNoRec $JavaMicroUI _java_[A]*[LP]ej_microui.* _java_[A]*[LP]ej_drawing.* _java_[A]*[LP]com_is2t_microbsp_microui.* _java_[A]*[LP]com_is2t_microui.* _java_[A]*[LP]com_is2t_debug_Assert.* _java_[A]*[LP]com_is2t_microej_microui.* _java_Pcom_is2t_microej _java_Pcom_is2t_microbsp.* _java_Presources _java_Presources_fonts _java_Pcom_is2t_debug _java_[A]*[LP]com_is2t_pump.*
createGraphNoRec $JavaMicroVG _java_[A]*[LP]ej_microvg.* 
createGraphNoRec $JavaMWT _java_[A]*[LP]ej_mwt.* _java_[A]*[LP]com_is2t_mwt.*
createGraphNoRec $JavaDomino _java_[A]*[LP]com_is2t_domino.*
createGraphNoRec $JavaNUM _java_[A]*[LP]ej_numeric.* _java_[A]*[LP]com_is2t_err_NumericErrorMessages.*
createGraphNoRec $JavaOSGiME _java_[A]*[LP]org_osgi_framework_.*
createGraphNoRec $JavaNLS _java_[A]*[LP]ej_nls.* _java_[A]*[LP]com_microej_nls.*
createGraphNoRec $JavaSNI _java_[A]*[LP]ej_sni.*
createGraphNoRec $JavaSP _java_[A]*[LP]com_is2t_sp_.*
createGraphNoRec $JavaStoryBoard _java_[A]*[LP]com_is2t_storyboard_.*
createGraphNoRec $JavaWidget _java_[A]*[LP]ej_mwt.widgets.* _java_[A]*[LP]com_is2t_mwt.widgets.* _java_[A]*[LP]ej_color.* _java_[A]*[LP]ej_container.* _java_[A]*[LP]ej_style.* _java_[A]*[LP]ej_widget.* _java_[A]*[LP]ej_navigation.* _java_[A]*[LP]ej_animation.*
createGraphNoRec $JavaInternetPack _java_[A]*[LP]com_is2t_server.* _java_[A]*[LP]com_is2t_template_parser.*
createGraphNoRec $Logging _java_[A]*[LP]java_util_logging.* _java_[A]*[LP]com_is2t_logging.* _java_[A]*[LP]ej_util_logging_.* _java_Pej_util_logging.*
createGraphNoRec $Basictool _java_[A]*[LP]ej_basictool.*
createGraphNoRec $Components _java_[A]*[LP]ej_components.*
createGraphNoRec $Service _java_[A]*[LP]ej_service.*
createGraphNoRec $Wadapps _java_[A]*[LP]ej_wadapps.* _java_[A]*[LP]com_microej_wadapps.*
createGraphNoRec $RCommand _java_[A]*[LP]ej_rcommand.* _java_[A]*[LP]ej_library_iot_rcommand.*
createGraphNoRec $Websocket _java_..?ej_websocket.*
createGraphNoRec $SNTPClient _java_.android_net_SntpClient.*
createGraphNoRec $Motion _java_[A]*[LP]ej_motion.*
createGraphNoRec $Gesture _java_[A]*[LP]ej_gesture.* _java_[A]*[LP]com_is2t_gestures.*
createGraphNoRec $FlowMWT _java_[A]*[LP]ej_flow_mwt.*
createGraphNoRec $Flow _java_[A]*[LP]ej_flow.*
createGraphNoRec $AllJoyn _java_[A]*[LP]org_alljoyn.*
createGraphNoRec $TestSuite _java_[A]*[LP]com_is2t_testsuite.*
createGraphNoRec $JavaNET _java_[A]*[LP]com_is2t_errors_NetErrors$1.* _java_[A]*[LP]com_is2t_errors_NetErrors$Messages.* _java_[A]*[LP]com_is2t_errors_NetErrors.* _java_[A]*[LP]com_is2t_support_DependencyInjectionHelper.* _java_[A]*[LP]com_is2t_support_net_Channel$Kind.* _java_[A]*[LP]com_is2t_support_net_Channel$StateImpl.* _java_[A]*[LP]com_is2t_support_net_Channel.* _java_[A]*[LP]com_is2t_support_net_DatagramSocketChannel.* _java_[A]*[LP]com_is2t_support_net_DatagramSocketChannelFactory.* _java_[A]*[LP]com_is2t_support_net_IChannel$State.* _java_[A]*[LP]com_is2t_support_net_IChannel.* _java_[A]*[LP]com_is2t_support_net_IDNS.* _java_[A]*[LP]com_is2t_support_net_IDatagramSocketChannel.* _java_[A]*[LP]com_is2t_support_net_IDatagramSocketChannelFactory.* _java_[A]*[LP]com_is2t_support_net_IDatagramSocketImplFactory.* _java_[A]*[LP]com_is2t_support_net_IMulticastSocketChannel.* _java_[A]*[LP]com_is2t_support_net_INetworkAddress.* _java_[A]*[LP]com_is2t_support_net_INetworkInterface.* _java_[A]*[LP]com_is2t_support_net_INetworkInterfaceData.* _java_[A]*[LP]com_is2t_support_net_ISocketChannel.* _java_[A]*[LP]com_is2t_support_net_IStreamSocketChannel.* _java_[A]*[LP]com_is2t_support_net_IStreamSocketChannelFactory.* _java_[A]*[LP]com_is2t_support_net_NetworkAddress.* _java_[A]*[LP]com_is2t_support_net_NetworkInterface.* _java_[A]*[LP]com_is2t_support_net_NetworkResources.* _java_[A]*[LP]com_is2t_support_net_PlainDatagramSocketImpl.* _java_[A]*[LP]com_is2t_support_net_PlainSocketImpl.* _java_[A]*[LP]com_is2t_support_net_SocketChannel.* _java_[A]*[LP]com_is2t_support_net_StreamSocketChannel.* _java_[A]*[LP]com_is2t_support_net_StreamSocketChannelFactory.* _java_[A]*[LP]com_is2t_support_net_VMNetworkInterfaceData.* _java_[A]*[LP]com_is2t_support_net_natives_ChannelNatives.* _java_[A]*[LP]com_is2t_support_net_natives_DatagramSocketChannelNatives.* _java_[A]*[LP]com_is2t_support_net_natives_NetworkAddressNatives.* _java_[A]*[LP]com_is2t_support_net_natives_NetworkInterfaceNatives.* _java_[A]*[LP]com_is2t_support_net_natives_SocketChannelNatives.* _java_[A]*[LP]com_is2t_support_net_natives_StreamSocketChannelNatives.* _java_[A]*[LP]com_is2t_support_net_security_INetSecurityManager.* _java_[A]*[LP]com_is2t_support_net_security_INetSecurityManagerFactory.* _java_[A]*[LP]com_is2t_support_net_security_IURLSecurityManager.* _java_[A]*[LP]com_is2t_support_net_security_NetSecurityManagerFactory.* _java_[A]*[LP]com_is2t_support_net_security_NetSecurityManagerStub.* _java_[A]*[LP]com_is2t_support_net_security_URLSecurityManagerStub.* _java_[A]*[LP]com_is2t_support_net_util_IntRef.* _java_[A]*[LP]com_is2t_support_net_util_NetTools.* _java_[A]*[LP]java_net_AbstractPlainDatagramSocketImpl.* _java_[A]*[LP]java_net_AbstractPlainSocketImpl.* _java_[A]*[LP]java_net_BindException.* _java_[A]*[LP]java_net_ConnectException.* _java_[A]*[LP]java_net_DatagramPacket.* _java_[A]*[LP]java_net_DatagramSocket.* _java_[A]*[LP]java_net_DatagramSocketImpl.* _java_[A]*[LP]java_net_DatagramSocketImplFactory.* _java_[A]*[LP]java_net_DefaultDatagramSocketImplFactory.* _java_[A]*[LP]java_net_Inet4Address.* _java_[A]*[LP]java_net_Inet6Address$1.* _java_[A]*[LP]java_net_Inet6Address$Inet6AddressHolder.* _java_[A]*[LP]java_net_Inet6Address.* _java_[A]*[LP]java_net_InetAddress$InetAddressHolder.* _java_[A]*[LP]java_net_InetAddress.* _java_[A]*[LP]java_net_InetSocketAddress$1.* _java_[A]*[LP]java_net_InetSocketAddress$InetSocketAddressHolder.* _java_[A]*[LP]java_net_InetSocketAddress.* _java_[A]*[LP]java_net_InterfaceAddress.* _java_[A]*[LP]java_net_MulticastSocket.* _java_[A]*[LP]java_net_NetPermission.* _java_[A]*[LP]java_net_NetworkInterface$1.* _java_[A]*[LP]java_net_NetworkInterface$1checkedAddresses.* _java_[A]*[LP]java_net_NetworkInterface.* _java_[A]*[LP]java_net_NoRouteToHostException.* _java_[A]*[LP]java_net_PortUnreachableException.* _java_[A]*[LP]java_net_ServerSocket.* _java_[A]*[LP]java_net_Socket.* _java_[A]*[LP]java_net_SocketAddress.* _java_[A]*[LP]java_net_SocketException.* _java_[A]*[LP]java_net_SocketImpl.* _java_[A]*[LP]java_net_SocketImplFactory.* _java_[A]*[LP]java_net_SocketInputStream.* _java_[A]*[LP]java_net_SocketOptions.* _java_[A]*[LP]java_net_SocketOutputStream.* _java_[A]*[LP]java_net_SocketPermission.* _java_[A]*[LP]java_net_SocketTimeoutException.* _java_[A]*[LP]java_net_UnknownHostException.* _java_[A]*[LP]javax_net_DefaultServerSocketFactory.* _java_[A]*[LP]javax_net_DefaultSocketFactory.* _java_[A]*[LP]javax_net_ServerSocketFactory.* _java_[A]*[LP]javax_net_SocketFactory.* _java_[A]*[LP]sun_net_ConnectionResetException.* _java_[A]*[LP]sun_net_util_IPAddressUtil.* _java_[A]*[LP]com_is2t_support_net_dns_SoftIPv4DNSResolver.* _java_[A]*[LP]com_is2t_support_net_natives_SoftDNSNatives.* _java_[A]*[LP]ej_net_dns_DNSConnection$1.* _java_[A]*[LP]ej_net_dns_DNSConnection.* _java_[A]*[LP]ej_net_dns_DNSConstants.* _java_[A]*[LP]ej_net_dns_DNSEntry.* _java_[A]*[LP]ej_net_dns_DNSFormatException.* _java_[A]*[LP]ej_net_dns_DNSResolver.* _java_[A]*[LP]ej_net_dns_DatagramSocketImplConnectionFactory.* _java_[A]*[LP]ej_net_dns_IDatagramConnection.* _java_[A]*[LP]ej_net_dns_IDatagramConnectionFactory.* _java_[A]*[LP]java_net_DatagramSocketImplConnection.*  _java_Pjava_net  _java_Pjavax_net  _java_Psun_net _java_[A]*[LP]com_is2t_support_net.* 
createGraphNoRec $JavaBLUETOOTH _java_[A]*[LP]ej_bluetooth.*
createGraphNoRec $JavaHAL _java_[A]*[LP]ej_hal.* _java_[A]*[LP]com_is2t_hal.*
createGraphNoRec $JavaFS _java_[A]*[LP]com_is2t_java_io_DefaultFileChannel.* _java_[A]*[LP]com_is2t_java_io_DirChannel.* _java_[A]*[LP]com_is2t_java_io_FatFsFileSystem.* _java_[A]*[LP]com_is2t_java_io_FileSystemResources.* _java_[A]*[LP]com_is2t_java_io_GenericFileSystem.* _java_[A]*[LP]com_is2t_java_io_FileSystem.* _java_[A]*[LP]com_is2t_java_io_IFileChannel$OpenMode.* _java_[A]*[LP]com_is2t_java_io_IFileChannel.* _java_[A]*[LP]com_is2t_java_io_IFileSystem.* _java_[A]*[LP]com_is2t_java_io_UnixFileSystem.* _java_[A]*[LP]com_is2t_java_io_UnixLikeFileSystem.* _java_[A]*[LP]com_is2t_java_io_WinNTFileSystem.* _java_[A]*[LP]java_io_File$PathStatus.* _java_[A]*[LP]java_io_File$TempDirectory.* _java_[A]*[LP]java_io_File.* _java_[A]*[LP]java_io_FileFilter.* _java_[A]*[LP]java_io_FileInputStream.* _java_[A]*[LP]java_io_FileNotFoundException.* _java_[A]*[LP]java_io_FileOutputStream.* _java_[A]*[LP]java_io_FilePermission.* _java_[A]*[LP]java_io_FilenameFilter.*
createGraphNoRec $JavaSSL _java_[A]*[LP]com_is2t_support_net_ssl.* _java_[A]*[LP]com_is2t_support_pool_RecycleWeakRefPool.* _java_[A]*[LP]com_is2t_support_pool_RecycleWeakRefPoolFeature.* _java_[A]*[LP]com_is2t_support_pool_RecycleWeakRefPoolKernel.* _java_[A]*[LP]com_is2t_support_pool_RecycleWeakReference.* _java_[A]*[LP]com_is2t_support_pool_WeakReferenceClose.* _java_[A]*[LP]java_security_GeneralSecurityException.* _java_[A]*[LP]java_security_InvalidAlgorithmParameterException.* _java_[A]*[LP]java_security_KeyException.* _java_[A]*[LP]java_security_KeyManagementException.* _java_[A]*[LP]java_security_KeyStore.* _java_[A]*[LP]java_security_KeyStoreException.* _java_[A]*[LP]java_security_KeyStoreSpi.* _java_[A]*[LP]java_security_NoSuchAlgorithmException.* _java_[A]*[LP]java_security_SecureRandom.* _java_[A]*[LP]java_security_UnrecoverableEntryException.* _java_[A]*[LP]java_security_UnrecoverableKeyException.* _java_[A]*[LP]java_security_cert_Certificate.* _java_[A]*[LP]java_security_cert_CertificateException.* _java_[A]*[LP]java_security_cert_CertificateFactory.* _java_[A]*[LP]java_security_cert_CertificateFactorySpi.* _java_[A]*[LP]java_security_cert_X509Certificate.* _java_[A]*[LP]java_security_cert_X509Extension.* _java_[A]*[LP]javax_net_ssl.* 
createGraphNoRec $JavaOrgJSON _java_[A]*[LP]org_json.*
createGraphNoRec $JavaEjJSON _java_[A]*[LP]ej_json.*
createGraphNoRec $JavaCBOR _java_[A]*[LP]ej_cbor_.* _java_Pej_cbor
createGraphNoRec $JavaPaho _java_[A]*[LP]org_eclipse_paho.* _java_[A]*[LP]com_microej_paho.* _java_Porg_eclipse
createGraphNoRec $JavaCalifornium_Blockwise _java_Lorg_eclipse_californium_core_network_stack_Blockwise.* _java_Lorg_eclipse_californium_core_coap_BlockOption.*
createGraphNoRec $JavaCalifornium_Observe _java_Lorg_eclipse_californium_core_network_stack_Observe.* _java_Lorg_eclipse_californium_core_observe_.*
createGraphNoRec $JavaCalifornium_Deduplication_Crop _java_Lorg_eclipse_californium_core_network_deduplication_CropRotation.*
createGraphNoRec $JavaCalifornium_Deduplication_Sweep _java_Lorg_eclipse_californium_core_network_deduplication_SweepDeduplicator.*
createGraphNoRec $JavaCalifornium_Connector_UDP _java_Lorg_eclipse_californium_elements_.*
createGraphNoRec $JavaCalifornium _java_[A]*[LP]org_eclipse_californium_.*
createGraphNoRec $JavaLwm2m_Models _java_[A]*[LP]ej_lwm2m_models_.*
createGraphNoRec $JavaLwm2m_Californium _java_[A]*[LP]ej_lwm2m_coap_.*
createGraphNoRec $JavaLwm2m_API _java_[A]*[LP]ej_lwm2m_.*
createGraphNoRec $JavaNetExt _java_[A]*[LP]ej_ecom_network_.* _java_Lej_ecom_wifi_.*
createGraphNoRec $JavaREST _java_[A]*[LP]ej_rest.*
createGraphNoRec $JavaObservable _java_[A]*[LP]ej_observable.*
createGraphNoRec $JavaTrace _java_[LP]ej_trace.* __icetea___6rodata_6TRACE.* Java_ej_trace_.*  MJVM_MONITOR_.* TRACE_.*
createGraphNoRec $JavaSecurity _java_[A]*[LP]com_is2t_support_security.* _java_[A]*[LP]javax_crypto.* _java_[A]*[LP]javax_security.* _java_[A]*[LP]java_security.* _java_[A]*[LP]com_microej_security.*  
createGraphNoRec $JavaDTLS _java_[A]*[LP]com_is2t_support_net_ssl_natives_DTLS.* _java_[A]*[LP]com_is2t_support_net_ssl_DTLS.* _java_[A]*[LP]com_microej_net_ssl_DTLS.* _java_[A]*[LP]com_is2t_support_net_ssl_AbstractDTLS.*
createGraphNoRec $JavaDevice _java_[A]*[LP]ej_util_Device.*


createGraphNoRec LibAddonEclasspath_URLUTIL _java_[A]*[LP]java_net_URLDecoder.* _java_[A]*[LP]java_net_URLEncoder.* 
createGraphNoRec LibAddonEclasspath_URL _java_[A]*[LP]java_net_ContentHandler.* _java_[A]*[LP]java_net_ContentHandlerFactory.* _java_[A]*[LP]java_net_MalformedURLException.* _java_[A]*[LP]java_net_NetPermission.* _java_[A]*[LP]java_net_Parts.* _java_[A]*[LP]java_net_ProtocolException.* _java_[A]*[LP]java_net_URL.* _java_[A]*[LP]java_net_URLConnection.* _java_[A]*[LP]java_net_URLStreamHandler.* _java_[A]*[LP]java_net_URLStreamHandlerFactory.* _java_[A]*[LP]java_net_UnknownContentHandler.* _java_[A]*[LP]java_net_UnknownServiceException.* _java_[A]*[LP]sun_net_DefaultProgressMeteringPolicy.* _java_[A]*[LP]sun_net_ProgressEvent.* _java_[A]*[LP]sun_net_ProgressListener.* _java_[A]*[LP]sun_net_ProgressMeteringPolicy.* _java_[A]*[LP]sun_net_ProgressMonitor.* _java_[A]*[LP]sun_net_ProgressSource$State.* _java_[A]*[LP]sun_net_ProgressSource.* _java_[A]*[LP]sun_net_util_IPAddressUtil.* _java_[A]*[LP]sun_net_www_MessageHeader$HeaderIterator.* _java_[A]*[LP]sun_net_www_MessageHeader.*  _java_Psun_net_util 
createGraphNoRec LibAddonEclasspath_URI _java_[A]*[LP]java_net_URI$Parser.* _java_[A]*[LP]java_net_URI.* _java_[A]*[LP]java_net_URISyntaxException.* 
createGraphNoRec LibAddonEclasspath_STRINGTOKENIZER _java_[A]*[LP]java_util_StringTokenizer.* 
createGraphNoRec LibAddonEclasspath_SOCKETFACTORY _java_[A]*[LP]javax_net_DefaultServerSocketFactory.* _java_[A]*[LP]javax_net_DefaultSocketFactory.* _java_[A]*[LP]javax_net_ServerSocketFactory.* _java_[A]*[LP]javax_net_SocketFactory.* 
createGraphNoRec LibAddonEclasspath_QUEUES _java_[A]*[LP]java_util_ArrayDeque$1.* _java_[A]*[LP]java_util_ArrayDeque$DeqIterator.* _java_[A]*[LP]java_util_ArrayDeque$DescendingIterator.* _java_[A]*[LP]java_util_ArrayDeque.* 
createGraphNoRec LibAddonEclasspath_PROPERTIES _java_[A]*[LP]java_util_Properties$LineReader.* _java_[A]*[LP]java_util_Properties.* 
createGraphNoRec LibAddonEclasspath_PRINTWRITER _java_[A]*[LP]java_io_PrintWriter.* 
createGraphNoRec LibAddonEclasspath_PIPEDSTREAMS _java_[A]*[LP]java_io_PipedInputStream.* _java_[A]*[LP]java_io_PipedOutputStream.* _java_[A]*[LP]java_io_PipedReader.* _java_[A]*[LP]java_io_PipedWriter.* 
createGraphNoRec LibAddonEclasspath_MAPS _java_[A]*[LP]java_util_TreeMap$AscendingSubMap$AscendingEntrySetView.* _java_[A]*[LP]java_util_TreeMap$AscendingSubMap.* _java_[A]*[LP]java_util_TreeMap$DescendingKeyIterator.* _java_[A]*[LP]java_util_TreeMap$DescendingSubMap$DescendingEntrySetView.* _java_[A]*[LP]java_util_TreeMap$DescendingSubMap.* _java_[A]*[LP]java_util_TreeMap$Entry.* _java_[A]*[LP]java_util_TreeMap$EntryIterator.* _java_[A]*[LP]java_util_TreeMap$EntrySet.* _java_[A]*[LP]java_util_TreeMap$KeyIterator.* _java_[A]*[LP]java_util_TreeMap$KeySet.* _java_[A]*[LP]java_util_TreeMap$NavigableSubMap$DescendingSubMapEntryIterator.* _java_[A]*[LP]java_util_TreeMap$NavigableSubMap$DescendingSubMapKeyIterator.* _java_[A]*[LP]java_util_TreeMap$NavigableSubMap$EntrySetView.* _java_[A]*[LP]java_util_TreeMap$NavigableSubMap$SubMapEntryIterator.* _java_[A]*[LP]java_util_TreeMap$NavigableSubMap$SubMapIterator.* _java_[A]*[LP]java_util_TreeMap$NavigableSubMap$SubMapKeyIterator.* _java_[A]*[LP]java_util_TreeMap$NavigableSubMap.* _java_[A]*[LP]java_util_TreeMap$PrivateEntryIterator.* _java_[A]*[LP]java_util_TreeMap$ValueIterator.* _java_[A]*[LP]java_util_TreeMap$Values.* _java_[A]*[LP]java_util_TreeMap.* 
createGraphNoRec LibAddonEclasspath_LOGGING _java_[A]*[LP]ej_util_logging_handler_DefaultHandler.* _java_[A]*[LP]java_util_logging_Handler.* _java_[A]*[LP]java_util_logging_Level.* _java_[A]*[LP]java_util_logging_LogManager$1.* _java_[A]*[LP]java_util_logging_LogManager$RootLogger.* _java_[A]*[LP]java_util_logging_LogManager.* _java_[A]*[LP]java_util_logging_LogRecord.* _java_[A]*[LP]java_util_logging_Logger.* 
createGraphNoRec LibAddonEclasspath_LISTS _java_[A]*[LP]java_util_LinkedList$1.* _java_[A]*[LP]java_util_LinkedList$DescendingIterator.* _java_[A]*[LP]java_util_LinkedList$ListItr.* _java_[A]*[LP]java_util_LinkedList$Node.* _java_[A]*[LP]java_util_LinkedList.* 
createGraphNoRec LibAddonEclasspath_HTTPSCLIENT _java_[A]*[LP]javax_net_ssl_HttpsURLConnection.* _java_Psun_net_www_protocol_https _java_[A]*[LP]sun_net_www_protocol_https_AbstractDelegateHttpsURLConnection.* _java_[A]*[LP]sun_net_www_protocol_https_DelegateHttpsURLConnection.* _java_[A]*[LP]sun_net_www_protocol_https_Handler.* _java_[A]*[LP]sun_net_www_protocol_https_HttpsClient.* _java_[A]*[LP]sun_net_www_protocol_https_HttpsURLConnectionImpl.* 
createGraphNoRec LibAddonEclasspath_HTTPCLIENT _java_[A]*[LP]java_net_HttpRetryException.* _java_[A]*[LP]java_net_HttpURLConnection.* _java_[A]*[LP]sun_net_NetworkClient.* _java_[A]*[LP]sun_net_www_http.* _java_[A]*[LP]sun_net_www_http.* _java_[A]*[LP]sun_net_www_HeaderParser.* _java_[A]*[LP]sun_net_www_protocol_http.* 
createGraphNoRec LibAddonEclasspath_EXECUTOR _java_[A]*[LP]java_util_concurrent_Callable.* _java_[A]*[LP]java_util_concurrent_CancellationException.* _java_[A]*[LP]java_util_concurrent_Delayed.* _java_[A]*[LP]java_util_concurrent_ExecutionException.* _java_[A]*[LP]java_util_concurrent_Executor.* _java_[A]*[LP]java_util_concurrent_ExecutorService.* _java_[A]*[LP]java_util_concurrent_Future.* _java_[A]*[LP]java_util_concurrent_RejectedExecutionException.* _java_[A]*[LP]java_util_concurrent_ScheduledExecutorService.* _java_[A]*[LP]java_util_concurrent_ScheduledFuture.* _java_[A]*[LP]java_util_concurrent_ThreadFactory.* _java_[A]*[LP]java_util_concurrent_TimeUnit$1.* _java_[A]*[LP]java_util_concurrent_TimeUnit$2.* _java_[A]*[LP]java_util_concurrent_TimeUnit$3.* _java_[A]*[LP]java_util_concurrent_TimeUnit$4.* _java_[A]*[LP]java_util_concurrent_TimeUnit$5.* _java_[A]*[LP]java_util_concurrent_TimeUnit$6.* _java_[A]*[LP]java_util_concurrent_TimeUnit$7.* _java_[A]*[LP]java_util_concurrent_TimeUnit.* _java_[A]*[LP]java_util_concurrent_TimeoutException.* _java_[A]*[LP]_java_Lej_util_concurrent_SingleThreadExecutor.* 
createGraphNoRec LibAddonEclasspath_COLLECTIONSUTIL _java_[A]*[LP]java_util_Arrays$ArrayList.* _java_[A]*[LP]java_util_Arrays.* _java_[A]*[LP]java_util_Collections$1.* _java_[A]*[LP]java_util_Collections$2.* _java_[A]*[LP]java_util_Collections$AsLIFOQueue.* _java_[A]*[LP]java_util_Collections$CopiesList.* _java_[A]*[LP]java_util_Collections$EmptyEnumeration.* _java_[A]*[LP]java_util_Collections$EmptyIterator.* _java_[A]*[LP]java_util_Collections$EmptyList.* _java_[A]*[LP]java_util_Collections$EmptyListIterator.* _java_[A]*[LP]java_util_Collections$EmptyMap.* _java_[A]*[LP]java_util_Collections$EmptySet.* _java_[A]*[LP]java_util_Collections$ReverseComparator.* _java_[A]*[LP]java_util_Collections$ReverseComparator2.* _java_[A]*[LP]java_util_Collections$SelfComparable.* _java_[A]*[LP]java_util_Collections$SetFromMap.* _java_[A]*[LP]java_util_Collections$SingletonList.* _java_[A]*[LP]java_util_Collections$SingletonMap.* _java_[A]*[LP]java_util_Collections$SingletonSet.* _java_[A]*[LP]java_util_Collections$SynchronizedCollection.* _java_[A]*[LP]java_util_Collections$SynchronizedList.* _java_[A]*[LP]java_util_Collections$SynchronizedMap.* _java_[A]*[LP]java_util_Collections$SynchronizedRandomAccessList.* _java_[A]*[LP]java_util_Collections$SynchronizedSet.* _java_[A]*[LP]java_util_Collections$SynchronizedSortedMap.* _java_[A]*[LP]java_util_Collections$SynchronizedSortedSet.* _java_[A]*[LP]java_util_Collections$UnmodifiableCollection$1.* _java_[A]*[LP]java_util_Collections$UnmodifiableCollection.* _java_[A]*[LP]java_util_Collections$UnmodifiableList$1.* _java_[A]*[LP]java_util_Collections$UnmodifiableList.* _java_[A]*[LP]java_util_Collections$UnmodifiableMap$UnmodifiableEntrySet$1.* _java_[A]*[LP]java_util_Collections$UnmodifiableMap$UnmodifiableEntrySet$UnmodifiableEntry.* _java_[A]*[LP]java_util_Collections$UnmodifiableMap$UnmodifiableEntrySet.* _java_[A]*[LP]java_util_Collections$UnmodifiableMap.* _java_[A]*[LP]java_util_Collections$UnmodifiableRandomAccessList.* _java_[A]*[LP]java_util_Collections$UnmodifiableSet.* _java_[A]*[LP]java_util_Collections$UnmodifiableSortedMap.* _java_[A]*[LP]java_util_Collections$UnmodifiableSortedSet.* _java_[A]*[LP]java_util_Collections.* _java_[A]*[LP]java_util_ComparableTimSort.* _java_[A]*[LP]java_util_DualPivotQuicksort.* _java_[A]*[LP]java_util_Objects.* _java_[A]*[LP]java_util_TimSort.* 
createGraphNoRec LibAddonEclasspath_COLLECTIONS _java_[A]*[LP]java_util_AbstractQueue.* _java_[A]*[LP]java_util_AbstractSequentialList.* _java_[A]*[LP]java_util_Deque.* _java_[A]*[LP]java_util_NavigableMap.* _java_[A]*[LP]java_util_NavigableSet.* _java_[A]*[LP]java_util_Queue.* _java_[A]*[LP]java_util_SortedMap.* _java_[A]*[LP]java_util_SortedSet.* 
createGraphNoRec LibAddonEclasspath_BYTEBUFFER _java_[A]*[LP]java_nio_Bits.* _java_[A]*[LP]java_nio_Buffer.* _java_[A]*[LP]java_nio_BufferOverflowException.* _java_[A]*[LP]java_nio_BufferUnderflowException.* _java_[A]*[LP]java_nio_ByteBuffer.* _java_[A]*[LP]java_nio_ByteBufferAsCharBufferB.* _java_[A]*[LP]java_nio_ByteBufferAsCharBufferL.* _java_[A]*[LP]java_nio_ByteBufferAsCharBufferRB.* _java_[A]*[LP]java_nio_ByteBufferAsCharBufferRL.* _java_[A]*[LP]java_nio_ByteBufferAsDoubleBufferB.* _java_[A]*[LP]java_nio_ByteBufferAsDoubleBufferL.* _java_[A]*[LP]java_nio_ByteBufferAsDoubleBufferRB.* _java_[A]*[LP]java_nio_ByteBufferAsDoubleBufferRL.* _java_[A]*[LP]java_nio_ByteBufferAsFloatBufferB.* _java_[A]*[LP]java_nio_ByteBufferAsFloatBufferL.* _java_[A]*[LP]java_nio_ByteBufferAsFloatBufferRB.* _java_[A]*[LP]java_nio_ByteBufferAsFloatBufferRL.* _java_[A]*[LP]java_nio_ByteBufferAsIntBufferB.* _java_[A]*[LP]java_nio_ByteBufferAsIntBufferL.* _java_[A]*[LP]java_nio_ByteBufferAsIntBufferRB.* _java_[A]*[LP]java_nio_ByteBufferAsIntBufferRL.* _java_[A]*[LP]java_nio_ByteBufferAsLongBufferB.* _java_[A]*[LP]java_nio_ByteBufferAsLongBufferL.* _java_[A]*[LP]java_nio_ByteBufferAsLongBufferRB.* _java_[A]*[LP]java_nio_ByteBufferAsLongBufferRL.* _java_[A]*[LP]java_nio_ByteBufferAsShortBufferB.* _java_[A]*[LP]java_nio_ByteBufferAsShortBufferL.* _java_[A]*[LP]java_nio_ByteBufferAsShortBufferRB.* _java_[A]*[LP]java_nio_ByteBufferAsShortBufferRL.* _java_[A]*[LP]java_nio_ByteOrder.* _java_[A]*[LP]java_nio_CharBuffer.* _java_[A]*[LP]java_nio_DoubleBuffer.* _java_[A]*[LP]java_nio_FloatBuffer.* _java_[A]*[LP]java_nio_HeapByteBuffer.* _java_[A]*[LP]java_nio_HeapByteBufferR.* _java_[A]*[LP]java_nio_HeapCharBuffer.* _java_[A]*[LP]java_nio_HeapCharBufferR.* _java_[A]*[LP]java_nio_HeapDoubleBuffer.* _java_[A]*[LP]java_nio_HeapDoubleBufferR.* _java_[A]*[LP]java_nio_HeapFloatBuffer.* _java_[A]*[LP]java_nio_HeapFloatBufferR.* _java_[A]*[LP]java_nio_HeapIntBuffer.* _java_[A]*[LP]java_nio_HeapIntBufferR.* _java_[A]*[LP]java_nio_HeapLongBuffer.* _java_[A]*[LP]java_nio_HeapLongBufferR.* _java_[A]*[LP]java_nio_HeapShortBuffer.* _java_[A]*[LP]java_nio_HeapShortBufferR.* _java_[A]*[LP]java_nio_IntBuffer.* _java_[A]*[LP]java_nio_InvalidMarkException.* _java_[A]*[LP]java_nio_LongBuffer.* _java_[A]*[LP]java_nio_ReadOnlyBufferException.* _java_[A]*[LP]java_nio_ShortBuffer.* _java_[A]*[LP]java_nio_StringCharBuffer.* 
createGraphNoRec LibAddonEclasspath_BUFFEREDSTREAMS _java_[A]*[LP]java_io_BufferedInputStream.* _java_[A]*[LP]java_io_BufferedOutputStream.* _java_[A]*[LP]java_io_BufferedReader.* _java_[A]*[LP]java_io_BufferedWriter.* 
createGraphNoRec LibAddonEclasspath_BITSET _java_[A]*[LP]java_util_BitSet.* 
createGraphNoRec LibAddonEclasspath_SET _java_[A]*[LP]java_util_HashSet.* _java_[A]*[LP]java_util_TreeSet.*
createGraphNoRec LibAddonEclasspath_BASE64 _java_[A]*[LP]java_util_Base64.*
createGraphNoRec LibAddonEclasspath_DATEFORMAT _java_[A]*[LP]java_text_.*DateFormat.* _java_[A]*[LP]ej_util_text_.*Format.*
createGraphNoRec LibAddonEclasspath_FORMATTER _java_[A]*[LP]java_util_.*Format.* _java_[A]*[LP]sun_misc_FloatingDecimal.* _java_[A]*[LP]sun_misc_FormattedFloatingDecimal.*
createGraphNoRec LibAddonEclasspath_MATH _java_[A]*[LP]java_math_.* _java_[A]*[LP]sun_misc_DoubleConsts.* _java_[A]*[LP]sun_misc_DoubleConsts.* _java_[A]*[LP]sun_misc_FDBigInteger.* _java_[A]*[LP]sun_misc_FloatConsts.* _java_[A]*[LP]sun_misc_IntegerHelper.* 





# JAVALIBS Native
createGraphNoRec $NativeEDC ist_microjvm_symbols.* section=.rodata.ist_microjvm_symbols_ClassNameTable.* NATCLDCBON.* NATEDCBON.* section=.text.NATEDCBON.* section=.rodata.NATEDCBON.* section=.text.NATCLDCBON.* section=.rodata.NATCLDCBON.* ist_microjvm_NativesPool.* ist_microjvm_soar_NativesPoolLinker.* ist_mowana_vm_GenericNativesPool.* ist_mowana_vm_GenericNativesPoolLinker.*
createGraphNoRec $NativeBON section=IMMUTABLES_BLOCKS section=IMMUTABLES_BLOCKS com_is2t_bon_.* ist_mowana_vm_immutablesBlock.* ist_mowana_vm_ImmutablesBlockNativesPool.*
createGraphNoRec $NativePAP NATPAP.* com_is2t_pap.* section=_pap_gpio_.* 
createGraphNoRec $NativeECOMCOMM  com_is2t_ecom.* section=_ni_stackgroup_ECOM.* com_is2t_comm_.* section=.text..*com_is2t_comm_.* _1ecom_.* COMM_CONNECTIONS_TABLE __icetea__virtual___1ecom.* section=.bss.ecom.comm.* __icetea__virtual__com_is2t_comm_.* __icetea__getSingleton__com_is2t_comm. __icetea__getSingleton__com_is2t_comm.* __icetea__.*ecom.*comm.* section=.text.__icetea__.*___1ecom_17comm.* 
createGraphNoRec $NativeMicroUI section=_audio_out_stacks section=_input_pump_stacks section=_ni_stackgroup_MUI_.* NATMICROUI.* section=LCD.* section=lcd.* com_is2t_microui.* section=_display_.* section=.rodata.com_is2t_microui_.* section=.text.com_is2t_microui_.* section=.rodata.cosTable.* section=_touchscreen_table _1leds_.* _1audio_.* com_is2t_audio_.* com_is2t_leds_.* display_ILS_.* __icetea__virtual__com_ist_display.* __icetea__virtual___1display.* __icetea__virtual__com_is2t_display.* __icetea__getSingleton__com_is2t_display.* __icetea__getSingleton__com_is2t_libraries_events.* __icetea__virtual__com_is2t_inputs.* __icetea__virtual__com_is2t_libraries_events.* __icetea___6text_6IMAGE_.*
createGraphNoRec $NativeMWT NATMWT.* 
createGraphNoRec $NativeNUM NATNUM.* com_is2t_numeric.* section=.text.*com_is2t_numeric_.* pcoefs__iceTea__initialData
createGraphNoRec $NativeOSGiME NATOSGiME.*
createGraphNoRec $NativeSNI SNI_.*
createGraphNoRec $NativeSP NATSP.* _1sp__.* _java_Lcom_is2t_natives_sp_NShieldedPlug.* _java_Pcom_is2t_natives_sp section=.text.__icetea__getSingleton__com_is2t_sp_.* section=.text.__icetea__virtual__com_is2t_sp_.* section=.shieldedplug shieldedplug_.* _java_Pcom_is2t_natives com_is2t_natives_sp_.* SP_get.* SP_reset
createGraphNoRec $NativeStoryBoard NATStoryBoard.*
createGraphNoRec $NativeWidget NATWidgets.*
createGraphNoRec $NativeKF com_is2t_kf_KFNativesPool.* com_is2t_kf_KFDynamicLoaderNativesPool.* section=KERNEL_WORKING_BUFFER
createGraphNoRec $NativeHAL Java_ej_hal_gpio_GPIONatives.*
createGraphNoRec $NativeFS FS_ADAPT_.* Java_com_is2t_java_io_DefaultFileChannel.* Java_com_is2t_java_io_GenericFileSystem.* Java_com_is2t_java_io_UnixFileSystem.* Java_com_is2t_java_io_WinNTFileSystem.* LLFS_.* fs_worker.* Java_com_is2t_java_io_FileSystem.*  

# JAVALIBS Java + Native + Immutables
union $Label_EDC $JavaEDC $NativeEDC
union $Label_BON $JavaBON $NativeBON
union $Label_PAP $JavaPAP $NativePAP
union $Label_ECOM $JavaECOM $JavaECOM
union $Label_ECOMCOMM $JavaECOMCOMM $JavaECOMCOMM
union $Label_NativeStackECOMCOMM $NativeECOMCOMM $NativeECOMCOMM
union $Label_MicroUI $JavaMicroUI $JavaMicroUI
union $Label_MicroVG $JavaMicroVG $JavaMicroVG
union $Label_MWT $JavaMWT $NativeMWT
union $Label_Domino $JavaDomino $JavaDomino
union $Label_NUM $JavaNUM $NativeNUM
union $Label_OSGiME $JavaOSGiME $NativeOSGiME
union $Label_NLS $JavaNLS $JavaNLS
union $Label_SNI $JavaSNI $NativeSNI
union $Label_SP $JavaSP $NativeSP
union $Label_StoryBoard $JavaStoryBoard $NativeStoryBoard
union $Label_Widget $JavaWidget $NativeWidget
union $Label_Internet_Pack $JavaInternetPack $JavaInternetPack
union $Label_KF $JavaKF $NativeKF
union $Label_NET $JavaNET $JavaNET
union $Label_BLUETOOTH $JavaBLUETOOTH $JavaBLUETOOTH
union $Label_HAL $JavaHAL $NativeHAL
union $Label_FS $JavaFS $NativeFS
union $Label_SSL $JavaSSL $JavaSSL
union $Label_JSON $JavaOrgJSON $JavaEjJSON
union $Label_CBOR $JavaCBOR $JavaCBOR
union $Label_Paho $JavaPaho $JavaPaho
union $Label_Californium $JavaCalifornium $JavaCalifornium
union $Label_Californium_Blockwise $JavaCalifornium_Blockwise $JavaCalifornium_Blockwise
union $Label_Californium_Observe $JavaCalifornium_Observe $JavaCalifornium_Observe
union $Label_Californium_Deduplication_Crop $JavaCalifornium_Deduplication_Crop $JavaCalifornium_Deduplication_Crop
union $Label_Californium_Deduplication_Sweep $JavaCalifornium_Deduplication_Sweep $JavaCalifornium_Deduplication_Sweep
union $Label_Californium_Connector_UDP $JavaCalifornium_Connector_UDP $JavaCalifornium_Connector_UDP
union $Label_Lwm2m_Models $JavaLwm2m_Models $JavaLwm2m_Models
union $Label_Lwm2m_Californium $JavaLwm2m_Californium $JavaLwm2m_Californium
union $Label_Lwm2m_API $JavaLwm2m_API $JavaLwm2m_API
union $Label_NETEXT $JavaNetExt $JavaNetExt
union $Label_REST $JavaREST $JavaREST
union $Label_Observable $JavaObservable $JavaObservable
union $Label_Trace $JavaTrace $JavaTrace
union $Label_Security $JavaSecurity $JavaSecurity
union $Label_DTLS $JavaDTLS $JavaDTLS
union $Label_Device $JavaDevice $JavaDevice

createGraphNoRec $Label_InstalledFeatures _java_installed_features.* section=.rodata.installed.features.table.* section=.rodata.microej.features section=.text.features.installed section=.rodata.microej.features section=.bss.features.installed section=.bss.microej.features section=.bss.features.dynamic section=.bss.microej.features

# Native stacks
createGraphNoRec $NativeWolfSSL wolfSSL.* WOLFSSL_.* wc_.* initSSL initSSL_.* mp_add mp_clear mp_cmp mp_cmp_d mp_copy mp_count_bits mp_exptmod mp_init mp_init_multi mp_invmod mp_iszero mp_leading_bit mp_mod mp_montgomery_calc_normalization mp_montgomery_reduce mp_montgomery_setup mp_mul mp_mulmod mp_read_radix mp_read_unsigned_bin mp_rshb mp_set mp_sqr mp_to_unsigned_bin mp_unsigned_bin_size MakeMasterSecret MakeSSLv3 MakeSigner MakeTLSv1 MakeTLSv1_1 MakeTLSv1_2 MakeTlsMasterSecret InitCipherSpecs InitCiphers InitDecodedCert InitMutex InitSSL InitSSL_Ctx InitSSL_Method InitSuites IsAtLeastTLSv1_2 IsTLS FreeAltNames FreeArrays FreeCiphers FreeDecodedCert FreeHandshakeResources FreeMutex FreeNameSubtrees FreeSSL FreeSSL_Ctx FreeSigner FreeSignerTable GetAlgoId GetCA GetCAByName GetInt GetLength GetMyVersion GetSequence GetSet GrowInputBuffer  SendAlert SendBuffered SendCertificate SendCertificateVerify SendChangeCipher SendClientHello SendClientKeyExchange SendData SendFinished SetAlgoID SetCipherSpecs SetKeysSide SetLength SetSequence ShrinkInputBuffer ShrinkOutputBuffer StoreECC_DSA_Sig StoreKeys DecodeECC_DSA_Sig DecodeToKey DeriveKeys DeriveTlsKeys DoApplicationData DoFinished wolfTLSv1_1_client_method wolfTLSv1_2_client_method wolfTLSv1_client_method ValidateDate VerifyClientSuite ParseCert ParseCertRelative PemToDer ProcessReply ReceiveData AddCA AlreadySigner  AddHandShakeHeader Camellia_DecryptBlock Camellia_EncryptBlock ChachaAEADDecrypt ChachaAEADEncrypt CheckAvailableSize CheckCurve CipherRequires CleanPreMaster ConfirmNameConstraints ConfirmSignature ConstantCompare DecodeAltNames DecodeAuthInfo DecodeAuthKeyId DecodeBasicCaConstraint DecodeCertExtensions DecodeCrlDist DecodeExtKeyUsage DecodeKeyUsage DecodeNameConstraints DecodeSubjKeyId DecodeSubtree Decrypt Des3ProcessBlock DesRawProcessBlock DesSetKey DoAlert DoCertificate DoCertificateRequest DoHandShakeMsg DoHandShakeMsgType DoRounds DoServerHello DoServerKeyExchange GetCertHeader GetDate GetInputData GetKey GetLineCoding GetName GetObjectId GetRecordHeader GetSEQIncrement GetTime GrowOutputBuffer HashOutput HashSigner HmacKeyInnerHash InitSuitesHashSigAlgo MakeSslMasterSecret MatchBaseName MatchDomainName PadCheck Poly1305Tag Poly1305TagOld ProcessBuffer ProcessChainBuffer RsaUnPad SSL_CtxResourceFree SSL_ResourceFree SSL_hmac SanityCheckCipherText SanityCheckMsgReceived SetKeys SetPrefix TLS_hmac TimingPadVerify ToTraditional VerifyMac camellia_decrypt.* camellia_encrypt.* camellia_setup.* AddHeaders AddLength AddRecordHeader s_fp_add s_fp_sub Base64_Decode BuildCertHashes BuildFinished BuildMD5 BuildMessage BuildSHA BuildTlsFinished ByteReverseWords ByteReverseWords64 camellia_sp0222 camellia_sp1110 camellia_sp3033 camellia_sp4404 Transform i.Transform SECTION_Transform Td Te wolfssl_log ato16 base64Decode base64Encode BytePrecision c16toa c24to32 c32to24 c32toa cipher_names client DateGreaterThan des3CbcAlgoID desCbcAlgoID ECC_AlgoID ecc_.* Encrypt get_digit_count gmtime Hash_.* K K512 LockMutex master_label md2AlgoID md5AlgoID md5wRSA_AlgoID mystrnstr p_hash poly1305_blocks PRF rcon Receive rotrFixed64 RSA_AlgoID .*AddLength .*ByteReverseWords client .*xorbuf server sha.* sigma tau tls_.* U32TO8 U8TO32 UnLockMutex wolfssl.*   
createGraphNoRec $NativeMbedTLS mbedtls.* mbedtls_.* x509_.* ssl_.* secp.* ripemd.* camellia_.* des3_.* des_.* aes_.* ecdsa_.* eckey_.* ecp_.* gcm_.* md4_.* md5_.* oid_.* pem_.* pk_.* prng_.* rsa_.* SB1 SB2 SB3 SB4 SB5 SB6 SB7 SB8 RT0 RT1 RT2 RT3 tls1_.* entropy_.* entropy ciphersuite_.* FT0 FT1 FT2 FT3 ccm_auth_crypt mpi_mul_hlp FSb FSb1 FSb2 FSb3 FSb4 RSb mpi_montmul mgf_mask ctr_drbg_.* base64_dec_map last4 dhm_.* eckeydh_can_do ccm_.* brainpoolP256r1_.* pkcs12_.* brainpool.* blowfish_.* arc4_.* SECTION_mbedtls_.* SECTION_ssl_.* small_prime mpi_.* block_cipher_df   
createGraphNoRec $NativeLLNETSSL LLNET_SSL.* Java_com_is2t_support_net_ssl_natives_.* section=SSL_MEM
createGraphNoRec $NativeLWIP tcp_.* udp_.* lwip.* Lwip.* com_is2t_lwip.* dhcp_.* dns_.* netif_.* pbuf_.* netconn_.* ethernet_.* ethernetif_.* netconn_alloc etharp_.* inputUnit=.*/lwip/.*lwip_.*\.o inputUnit=.*/libLwip\.a.* sys_arch_mbox_fetch sys_arch_mbox_tryfetch sys_arch_protect sys_arch_sem_wait sys_arch_unprotect sys_init sys_mbox_free sys_mbox_new sys_mbox_post sys_mbox_set_invalid sys_mbox_trypost sys_mbox_valid sys_mutex_lock sys_mutex_new sys_mutex_unlock sys_sem_free sys_sem_new sys_sem_set_invalid sys_sem_signal sys_sem_valid sys_thread_new sys_timeout sys_timeouts_init sys_timeouts_mbox_fetch tcpip_apimsg tcpip_callback_with_block tcpip_init tcpip_input  icmp_dest_unreach icmp_input inet_chksum_pbuf ip4_addr_isbroadcast_u32 ip4_input ip4_output_if ip4_output_if_src ip4_route ip4addr_aton ip4addr_ntoa ip4addr_ntoa_r ip_addr_any ip_addr_broadcast ip_data ip_input ipaddr_addr netbuf_delete netbuf_free netbuf_ref memp_ARP_QUEUE memp_NETBUF memp_NETCONN memp_NETDB memp_PBUF memp_PBUF_POOL memp_SYS_TIMEOUT memp_TCPIP_MSG_API memp_TCPIP_MSG_INPKT memp_TCP_PCB memp_TCP_PCB_LISTEN memp_TCP_SEG memp_UDP_PCB memp_free memp_free_pool memp_init memp_init_pool memp_malloc memp_malloc_pool memp_memory_ARP_QUEUE_base memp_memory_NETBUF_base memp_memory_NETCONN_base memp_memory_NETDB_base memp_memory_PBUF_POOL_base memp_memory_PBUF_base memp_memory_SYS_TIMEOUT_base memp_memory_TCPIP_MSG_API_base memp_memory_TCPIP_MSG_INPKT_base memp_memory_TCP_PCB_LISTEN_base memp_memory_TCP_PCB_base memp_memory_TCP_SEG_base memp_memory_UDP_PCB_base ram_heap tcpip_.* tcphdr.* arp_.* igmp_.* icmp_.* ip4_.* ip_.* memp_.* mtu_.* netbuf_.* netifapi_.* raw_.* recv_.* sys_.* xid.* internal_netconn_tcp_server_socket_callback accept_function err_tcp poll_tcp sent_tcp mem_.* nd6_.* ip6_.* handle_dhcp icmp6_.* mld6_.* dhcps_.* ethip6_.*     
createGraphNoRec $NativeDispatch __iceTea__componentInit_dispatch_7events LLDISPATCH_.* _1dispatch_17events.*
createGraphNoRec $NativeLLNET LLNET_.* async_select.* DatagramSocketChannel_.* StreamSocketChannel_.* SocketChanel_.* Java_com_is2t_support_net_natives.*
createGraphNoRec $NativeLLSecurity Java_com_is2t_support_security.* LLSEC_.*
createGraphNoRec $NativeLLDTLS Java_com_is2t_support_net_ssl_natives_DTLS.*  
createGraphNoRec $NativeLLDevice Java_ej_util_Device.*
createGraphNoRec $NativeFATFS disk_initialize disk_ioctl disk_read disk_status disk_write f_chmod f_close f_closedir f_getfree f_lseek f_mkdir f_mount f_open f_opendir f_read f_readdir f_rename f_stat f_sync f_unlink f_utime f_write get_fat get_fattime FATFS_LinkDriver FATFS_LinkDriverEx SD_Driver SD_initialize SD_ioctl SD_read SD_status SD_write put_fat clust2sect .*FAT.*
createGraphNoRec $NativeFileX _fx_directory.* _fx_file.* _fx_media.* _fx_system.* _fx_utility.* _fx_version.*
createGraphNoRec $NativeNetX _nx_arp.* _nx_dhcp.* _nx_dns.* _nx_ether.* _nx_get.* _nx_icmp.* _nx_igmp.* _nx_ip.* _nx_packet.* _nx_rarp.* _nx_system.* _nx_tcp.* _nx_udp.* _nx_version.*
createGraphNoRec $NativeEscrypt EscAes.* EscDer.* EscEcc.* EscFeArith.* EscHashDrbg.* EscHmac.* EscModExpS0.* EscPkcs1RsaSsaV15.* EscPtArithWs.* EscRsa.* EscSha.* EscX509.* 
createGraphNoRec $NativeModel3G MODEM_3G_.*
createGraphNoRec $NativeWIFI WIFI_.* async_netconn_.* asyncOperation.* 
createGraphNoRec $NativeWIFITICC3100 CC3100_.* sl_.* _sl_.* _Sl.* .*SimpleLink.* SL_.* g_H2NCnysPattern g_H2NSyncPattern g_pCB pPingCallBackFunc RxMsgClassLUT StartResponseLUT 
createGraphNoRec $NativeCommandATManager CMD_MANAGER_.*
createGraphNoRec $NativeWICED wiced_.* wwd_.* wifi_.* wicedfs_.* network_tcp.* platform_.* sflash_.* host_platform_.* .*_wiced_.* host_rtos_.* host_buffer_.*
createGraphNoRec $NativeESP32_WIFI wpa_.* esp_wifi.* g_wifi.* phy_.* scan_.* sta_.* wlan_.* wpa2_.* wps_start SHA1Update SHA1Transform SHA1Init SHA1Final MD5Transform __wpa_send_eapol ieee80211_.* Te0 Td0 hostap_.* pp.* rijndael.* eapol_txcb resend_eapol_handle pm_.* esp_mesh.* mesh_.* _mesh_.* ethbroadcast r_lm_.* r_llm_.* cnx_.* _wifi_.* lld_.* hmac_md5_vector hmac_sha1_vector lmac.* coex_.*
createGraphNoRec $NativeESP32_BT bt_.* bta_.* btm_.* btc_.* btu_.* profile_tab gatt_.* config_new config_save bb_init multiprecision_.* smp_.* LLRCOMMAND_BLUETOOTH_.* BASE_UUID blufi_.* btdm_.* gatts_.* BTM_.* read_attr_value BT_.* reassemble_and_dispatch l2c_.* hci_.* GATTS_.* l2cble_.* l2ca_.* L2CA_.* l2cu_.* gap_.* GATTC_.* p_256_.* BTA_.* esp_bt_.* ECC_.* btsnd_.* gfm3_sbox gfm2_sbox sbox SECTION_base_uuid

# Remove LLNET_SSL pattern from $NativeLLNET
substract $NativeLLNET $NativeLLNET $NativeLLNETSSL

union $Label_NativeStackNET $NativeLWIP $NativeNetX
union $Label_NativeStackNET $Label_NativeStackNET $NativeDispatch $NativeCommandATManager $NativeLLNET
union $Label_NativeStackSSL $NativeWolfSSL $NativeMbedTLS $NativeLLNETSSL
union $Label_NativeStackDTLS $NativeLLDTLS $NativeLLDTLS
union $Label_NativeStackDevice $NativeLLDevice $NativeLLDevice
union $Label_NativeStackSecurity $NativeLLSecurity $NativeEscrypt
union $Label_NativeStackFS $NativeFATFS $NativeFileX
union $Label_NativeStackModem3G $NativeModel3G $NativeModel3G
union $Label_NativeStackWifi $NativeWIFI $NativeWIFI
union $Label_NativeStackWifiTICC3100 $NativeWIFITICC3100 $NativeWIFITICC3100
union $Label_NativeStackMicroUI $NativeMicroUI $NativeMicroUI
union $Label_NativeWICED $NativeWICED $NativeWICED
union $Label_NativeESP32_WIFI $NativeESP32_WIFI $NativeESP32_WIFI
union $Label_NativeESP32_BT $NativeESP32_BT $NativeESP32_BT

# LIBNetwork
createGraphNoRec $Label_LIBNetwork LIBNETWORK.* Macb.* .*NETWORK.*  .*mac.* .*socket.* com_ist_drivers_network.* com_is2t_drivers_nationalsemiconductor_phy.* section=_macb_rx.* section=_macb_tx.* section=_LWIP_ReceptionSection  section=_ITThreadStack section=_TCPIP_ThreadStack

#DRIVER
createGraphNoRec $Label_Drivers DRIVER.* section=.rodata.com_is2t_drivers.* section=.text.DRIVER.* section=.rodata.DRIVER.* section=.text.__icetea__inline__DRIVER.* com_ist_drivers.* com_is2t_drivers.* time_CurrentTime.* icetea_os_internal_InternCounter.* section=.rodata.com_ist_drivers_.* section=.text.__icetea__inline__com_ist_drivers_.* section=.text.com_ist_drivers_.* freq__iceTea__initialData com_is2t_leds.* section=.text..*com_is2t_leds.*

#Icetea Runtime
createGraphNoRec $Label_IceteaRuntime section=.*__iceTea__tabMallocINTERN  ICETEART.* com_is2t_startup.* ist_Default32bitsStartup.* ist_RegistryInitializationMethod.* com_ist_tools.* iceTea_boards.* .*_multiDimArrayAllocation_.* section=.text.iceTea_lang_.* section=.data.allocaPtr lib_externalAllocator__.* lib_math__iceTea__libInit section=.zero_init_table section=.relocation_data_table section=.rodata.icetea_components_init_table ist_SRelocationDataInterval.* ist_SZeroInitInterval.* inputUnit=.*/libgcc\.a.* section=.zero_init_table section=.rodata.zero_init_table section=.relocation_data_table section=.reset lib_externalAllocator.* lib_math.* _board_startup _program_start section=.text.vectors section=.rodata.relocation_data_table section=.text._board_initialization _icetea_support.* com_is2t_icetea_macro.* .*iceTea__initialData section=i.__iceTea__mallocINTERN section=.text.__icetea__getSingleton__iceTea_lang_.* section=.text.__icetea__virtual__iceTea_lang_.* section=.*__iceTea__long.* 

#OS
createGraphNoRec $Label_FreeRTOS xTask.* xTimer.* xQueue.* os.* vList.* vPort.* vQueue.* vTask.* xPort.* pvPortMalloc pvTaskGetThreadLocalStoragePointer pvTaskIncrementMutexHeldCount pvTimerGetTimerID pxCurrentTCB pxPortInitialiseStack uxListRemove uxQueueMessagesWaiting uxQueueSpacesAvailable ucHeap prv.* vApplication.* vIoeExpanderTaskFunction .*prvStartFirstTask .*prvEnableVFP uxCriticalNesting uxCurrentNumberOfTasks uxDeletedTasksWaitingCleanUp uxPendedTicks uxSchedulerSuspended uxTaskNumber uxTopReadyPriority uxTopUsedPriority xActiveTimerList.* xDelayedTaskList.* xIdleTaskHandle xLastTime.* xMaximumPossibleSuppressedTicks xNextTaskUnblockTime xNumOfOverflows xPendingReadyList xSchedulerRunning xSuspendedTaskList xTickCount xYieldPending xLastTime.* pthread_.* px.*
createGraphNoRec $Label_ICEOS OS.* section=.text.OS.* section=.rodata.OS.* icetea_os.* section=.text.icetea_os_.* section=.rodata.icetea_os_.* com_is2t_iceos_.*
createGraphNoRec $Label_ThreadX _tx_block.* _tx_build.* _tx_byte.* _tx_event.* _tx_initialize.* _tx_mutex.* _tx_queue.* _tx_semaphore.* _tx_thread.* _tx_time.* _tx_timer.* _txe_block.* _txe_byte.* _txe_event.* _txe_mutex.* _txe_queue.* _txe_semaphore.* _txe_thread.* _txe_timer.*
createGraphNoRec $Label_Mbed .*mbed(?!tls).*
union $Label_OS $Label_ICEOS $Label_FreeRTOS
union $Label_OS $Label_OS $Label_ThreadX $Label_Mbed

# VMCORE
createGraphNoRec $Label_VMCore VMCORE.* section=.text.asm.com_is2t_microjvm.* section=.text.asm.com_microej_microjvm.* section=.text.asm.VMCORE.* __icetea__inline__VMCORE.* section=.text.VMCORE.* section=.rodata.VMCORE.* ist_microjvm.* com_ist_vm_constants.* ist_mowana.* com_is2t_microjvm.* com_microej_microjvm.* com_is2t_vm_mowana.* section=.rodata.vm.tableswitch section=.data.vm.tableswitch.reloc section=.rodata.ist_mowana_.* section=.text.ist_microjvm_.* section=.text.ist_mowana_.* com_is2t_mowana.* section=.text.__icetea__inline__VMCORE.* section=.bss.vm.tableswitch.reloc section=.text.asm.ON_.* section=.rodataVmTableswitch section=.text.__icetea__getSingleton__com_is2t_microjvm_.* section=.text.__icetea__getSingleton__com_microej_microjvm_.* section=.text.__icetea__virtual__com_is2t_microjvm_.* section=.text.__icetea__virtual__com_microej_microjvm_.* section=.text.__icetea__getSingleton__VMCOREMicroJvm.* section=.bss.vm.stacks.gc section=.bss.vm.stacks.native section=.bss.vm.stacks.java section=.bss.microej.stacks _java_Pproperties MicroJvm_ILS_.* DRIVERMicroJvm.* MISCMicroJvm.* _switch_.* __icetea__virtual__com_is2t_microjvm.* __icetea__virtual__com_microej_microjvm.* __icetea__virtual__com_is2t_kf.* __icetea__getSingleton__com_is2t_microjvm.* __icetea__getSingleton__com_microej_microjvm.* __icetea__.*rodata.*kf.* com_is2t_kf_allocator_.* _java_soarfile_header_start _java_soarsection_header_start __icetea___6bss_6vm_6stacks_6java$$Base __icetea___6rodata_6MJVM_1MONITOR_.* __iceTea__componentInit_MicroJvm ____icetea__virtual__com_is2t_microjvm.* ____icetea__virtual__com_microej_microjvm.* ____icetea__virtual__com_is2t_kf_.* commonOPaload.* commonOPgetfield.* opreturn_.* OPthis_getfield_.*

# VMALLOC
createGraphNoRec $Label_VMAllocator section=_java_heap.* section=.bss.microej.heap section=EXTERNAL_ALLOCATOR section=_java_immortals section=.bss.microej.immortals VMALLOC.* section=.text.VMALLOC.* section=.rodata.VMALLOC.* ist_microjvm_allocator.* ist_mowana_vm_allocator.* com_is2t_microjvm_mowana_allocator.* com_microej_microjvm_mowana_allocator.* section=.text.com_ist_allocator_.* section=.rodata.ist_mowana_vm_allocator.*  section=.text.ist_mowana_vm_allocator.* com_ist_allocator.* allocator_ILS_.* 

# VMCONSOLE
createGraphNoRec $Label_VMConsole VMCONSOLE.* ist_microConsole.* com_is2t_microjvm_console.* com_microej_microjvm_console.* section=_CSL_.*

# VMDBG
createGraphNoRec $Label_VMDebugger VMDBG.* ist_debug.*

# LIBDISPLAY
createGraphNoRec $Label_LIBDisplay LIBDISPLAY.* __icetea__inline__LIBDISPLAY.* com_ist_display.* com_is2t_display.* section=.rodata.tabcosin.* section=.rodata.LIBDISPLAY.* com_ist_lcdDisplay.* section=.text.__icetea__inline__LIBDISPLAY.* section=.text.com_ist_display_.* section=.rodata.com_ist_display_.* section=.text.LIBDISPLAY.* section=.text.display.* _1display.* section=.text.__icetea__virtual__com_is2t_display.* section=.text.__icetea__getSingleton__com_is2t_display.* section=.text.__icetea__virtual___1display.* section=HEAP_DISPLAY_STACK section=.text..*com_is2t_display_.*

# LIBINPUT
createGraphNoRec $Label_LIBInput LIBINPUT.* __icetea__inline__LIBINPUT.* com.is2t.inputs.* section=.text.LIBINPUT.* com_is2t_inputs.* MISCinputs.* _1input.* section=HEAP_INPUTS_STACK section=.text..*com_is2t_inputs_.*

# LIBPNG
createGraphNoRec $Label_LIBPNG LIBPNG.* section=.text.LIBPNG.* section=.rodata.LIBPNG.* section=.text.com_ist_display_image_png_.* section=.rodata.ADAM7.* com_is2t_zip.* section=.rodata.tableCRC.* tableCRC.* section=.rodata.order.* section=.rodata.lbase.* section=.rodata.lext.* section=.rodata.dbase.* section=.rodata.dext.* section=.rodata.lenFixed.* section=.rodata.distFixed.* ADAM7.* com_is2t_display_image_PNGDecoder.* com_is2t_display_image_InternalImageDecoders___.*PNG lbase__iceTea__initialData lenFixed__iceTea__initialData lext__iceTea__initialData order__iceTea__initialData distFixed__iceTea__initialData dext__iceTea__initialData dbase__iceTea__initialData section=HEAP_PNG  

# LIBBMPM
createGraphNoRec $Label_LIBBMPM com_is2t_display_image_BMPMDecoder.* com_is2t_display_image_InternalImageDecoders___.*BMPM section=.text.IMAGE_.*

#IceTea Math
createGraphNoRec $Label_IceteaString iceTea_lang_character.* iceTea_lang_SystemOut.* iceTea_lang_String.*

# LIBFLOAT
createGraphNoRec $Label_LIBFloat LIBFLOAT.* iceTea_lang_Double.* iceTea_lang_Float.* fmod fmodf ff_.* fp_.*

# parse (Float/Double) toString(Float/Double)
createGraphNoRec $LIBFloatParseToString iceTea_lang_Double___parse.* iceTea_lang_Float___parse.* iceTea_lang_Double___toString.* iceTea_lang_Float___toString.* iceTea_lang_FPUtil.* section=.rodata.DOUBLE_F10__iceTea__initialData section=.rodata.DOUBLE_E10__iceTea__initialData section=.rodata.DOUBLE_F2__iceTea__initialData section=.rodata.DOUBLE_E2__iceTea__initialData section=.rodata.FLOAT_F10__iceTea__initialData section=.rodata.FLOAT_E10__iceTea__initialData section=.rodata.FLOAT_F2__iceTea__initialData section=.rodata.FLOAT_E2__iceTea__initialData  
union $Label_LIBFloat $Label_LIBFloat $LIBFloatParseToString

# Math (cos, sin, ...)
createGraphNoRec $LIBFloatMath iceTea_lang_Math.* __icetea__inline__iceTea_lang_Math.*
union $Label_LIBFloat $Label_LIBFloat $LIBFloatMath

# LIBFloat FPA
createGraphNoRec $LIBFloatFPA com_is2t_lib_FPA.* 
union $Label_LIBFloat $Label_LIBFloat $LIBFloatFPA

# LIBFloat GCC
createGraphNoRec $LIBFloatGCC LIBFloatGCC.*

createGraphNoRec $LIBFloatGCCArithmetic __addsf3 __adddf3 __addtf3 __addxf3 __subsf3 __subdf3 __subtf3 __subxf3 __mulsf3 __muldf3 __multf3 __mulxf3 __divsf3 __divdf3 __divtf3 __divxf3 __negsf2 __negdf2 __negtf2 __negxf2 
union $LIBFloatGCC $LIBFloatGCC $LIBFloatGCCArithmetic

createGraphNoRec $LIBFloatGCCConversion __extendsfdf2 __extendsftf2 __extendsfxf2 __extenddftf2 __extenddfxf2 __truncxfdf2 __trunctfdf2 __truncxfsf2 __trunctfsf2 __truncdfsf2 __fixsfsi __fixdfsi __fixtfsi __fixxfsi __fixsfdi __fixdfdi __fixtfdi __fixxfdi __fixsfti __fixdfti __fixtfti __fixxfti __fixunssfsi __fixunsdfsi __fixunstfsi __fixunsxfsi __fixunssfdi __fixunsdfdi __fixunstfdi __fixunsxfdi __fixunssfti __fixunsdfti __fixunstfti __fixunsxfti __floatsisf __floatsidf __floatsitf __floatsixf __floatdisf __floatdidf __floatditf __floatdixf __floattisf __floattidf __floattitf __floattixf __floatunsisf __floatunsidf __floatunsitf __floatunsixf __floatundisf __floatundidf __floatunditf __floatundixf __floatuntisf __floatuntidf __floatuntitf __floatuntixf
union $LIBFloatGCC $LIBFloatGCC $LIBFloatGCCConversion

createGraphNoRec $LIBFloatGCCComparaison __cmpsf2 __cmpdf2 __cmptf2 __unordsf2 __unorddf2 __unordtf2 __eqsf2 __eqdf2 __eqtf2 __nesf2 __nedf2 __netf2 __gesf2 __gedf2 __getf2 __ltsf2 __ltdf2 __lttf2 __lesf2 __ledf2 __letf2 __gtsf2 __gtdf2 __gttf2 
union $LIBFloatGCC $LIBFloatGCC $LIBFloatGCCComparaison

createGraphNoRec $LIBFloatGCCOther __powisf2 __powidf2 __powitf2 __powixf2 __mulsc3 __muldc3 __multc3 __mulxc3 __divsc3 __divdc3 __divtc3 __divxc3
union $LIBFloatGCC $LIBFloatGCC $LIBFloatGCCOther

createGraphNoRec $LIBFloatGCCConvert __d2b
union $LIBFloatGCC $LIBFloatGCC $LIBFloatGCCConvert

createGraphNoRec $LIBFloatGCCFunctions _cos _sin _ceil __ieee754_.* __kernel_cos __kernel_rem_pio2 __kernel_sin __fpclassify.* __fpcmp_parts_f __fpcmp_parts_d __pack_f __pack_d __make_dp __make_fp __thenan_df __thenan_sf __unpack_d __unpack_f __kernel_tan acos asin atan atan2 cosh exp fabs log log10 log1p nan pow rand rint scalbn sinh sqrt tan tanh __kernel_cosf __kernel_rem_pio2f __kernel_sinf __aeabi_dcmpeq __aeabi_dcmpge __aeabi_dcmpgt __aeabi_dcmple __aeabi_dcmplt atanf ceilf cosf fabsf nanf scalbnf sinf sdrtf _dtoa_r expm1 cbrt copysign copysignf finite floor floorf hypot matherr nextafter nextafterf sqrtf 
union $LIBFloatGCC $LIBFloatGCC $LIBFloatGCCFunctions

createGraphNoRec $LIBFloatKeil __aeabi_dadd __aeabi_dneg __aeabi_dsub __aeabi_drsub __aeabi_dmul __aeabi_ddiv  __aeabi_l2f __aeabi_i2d  __aeabi_ui2d __aeabi_l2d __aeabi_f2lz __aeabi_d2iz __aeabi_d2lz __aeabi_f2d __aeabi_cdcmple __aeabi_cdrcmple __aeabi_d2f __hardfp_.* __mathlib_dbl_.* __mathlib_flt_.* __aeabi_d2.* _float_.* _double_.* _drem _dsqrt _frem _fsqrt _fp_digits ceil cos sin __aeabi_f2ulz __ARM_scalbn __ARM_fpclassify __ARM_fpclassifyf __mathlib_expm1 __mathlib_log1p __mathlib_rredf2 __mathlib_zero __kernel_poly

# LIBFloat
union $Label_LIBFloat $Label_LIBFloat $LIBFloatGCC $LIBFloatKeil 

# USB
createGraphNoRec $Label_Usb USB_.* USBH_.*

# LIBInt GCC
createGraphNoRec $LIBIntGCC LIBIntGCC.*

# BSP Malloc
createGraphNoRec $Label_BSP_Malloc malloc free __malloc_av_ __malloc_lock __malloc_sbrk_base __malloc_trim_threshold __malloc_unlock __mb_cur_max _Balloc _Bfree _calloc_r _malloc_r _malloc_trim_r _realloc_r heap_caps.*

createGraphNoRec $LIBIntGCCArithmetic __ashlsi3 __ashldi3 __ashlti3 __ashrsi3 __ashrdi3 __ashrti3 __divsi3 __divdi3 __divti3 __lshrsi3 __lshrdi3 __lshrti3 __modsi3 __moddi3 __modti3 __mulsi3 __muldi3 __multi3 __negdi2 __negti2 __udivsi3 __udivdi3 __udivti3 __udivmoddi3 __udivti3 __umodsi3 __umoddi3 __umodti3 __aeabi_ldiv0 __lshift __gnu_ldivmod_helper __gnu_uldivmod_helper
union $LIBIntGCC $LIBIntGCC $LIBIntGCCArithmetic

createGraphNoRec $LIBIntGCCComparaison __cmpdi2 __cmpti2 __ucmpdi2 __ucmpti2
union $LIBIntGCC $LIBIntGCC $LIBIntGCCComparaison

createGraphNoRec $LIBIntGCCTrappingArithmetic __absvsi2 __absvdi2 __addvsi3 __addvdi3 __mulvsi3 __mulvdi3 __negvsi2 __negvdi2 __subvsi3 __subvdi3
union $LIBIntGCC $LIBIntGCC $LIBIntGCCTrappingArithmetic  

createGraphNoRec $LIBIntGCCBitOperations __clzsi2 __clzdi2 __clzti2 __ctzsi2 __ctzdi2 __ctzti2 __ffsdi2 __ffsti2 __paritysi2 __paritydi2 __parityti2 __popcountsi2 __popcountdi2 __popcountti2 __bswapsi2 __bswapdi2
union $LIBIntGCC $LIBIntGCC $LIBIntGCCBitOperations

createGraphNoRec $LIBIntKeil __aeabi_ldivmod __aeabi_llsl __aeabi_llsr	__aeabi_lasr __aeabi_uidivmod __aeabi_uldivmod __aeabi_ul2d

union $Label_LIBInt $LIBIntGCC $LIBIntKeil

createGraphNoRec $Label_PrintfScanf .*printf.* .*scanf.*
