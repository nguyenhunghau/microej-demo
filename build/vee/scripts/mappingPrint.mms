# MMS
#
# Copyright 2013-2023 IS2T. All rights reserved.
# Modification and distribution is permitted under certain conditions.
# IS2T PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.


# merge some sections
union $Label_MiscRuntime $Label_JavaRuntime $Label_JavaRuntime
union $Label_VMCore $Label_VMCore $Label_IceteaRuntime
union $Label_NativeStackMicroUI $Label_NativeStackMicroUI $Label_LIBDisplay
union $Label_NativeStackMicroUI $Label_NativeStackMicroUI $Label_LIBInput
union $Label_NativeStackMicroUI $Label_NativeStackMicroUI $Label_LIBPNG
union $Label_NativeStackMicroUI $Label_NativeStackMicroUI $Label_LIBBMPM


# Fix labels 
union VMDebugger $Label_VMDebugger $Label_VMDebugger
union VMConsole $Label_VMConsole $Label_VMConsole
union ApplicationStrings $Label_ApplicationStrings $Label_ApplicationStrings
union Statics $Label_Statics $Label_Statics
union Types $Label_Types $Label_Types
union LibFoundationEDC $Label_EDC $Label_EDC
union LibFoundationBON $Label_BON $Label_BON
union LibFoundationKF $Label_KF $Label_KF
union LibFoundationFS $Label_FS $Label_FS
union LibFoundationHAL $Label_HAL $Label_HAL
union LibFoundationSSL $Label_SSL $Label_SSL
union LibFoundationDTLS $Label_DTLS $Label_DTLS
union LibFoundationSecurity $Label_Security $Label_Security
union LibFoundationDevice $Label_Device $Label_Device
union LibFoundationNET $Label_NET $Label_NET
union LibFoundationBLUETOOTH $Label_BLUETOOTH $Label_BLUETOOTH
union LibFoundationPAP $Label_PAP $Label_PAP
union LibFoundationECOM $Label_ECOM $Label_ECOM
union LibFoundationECOM_COMM $Label_ECOMCOMM $Label_ECOMCOMM
union LibFoundationMicroUI $Label_MicroUI $Label_MicroUI
union LibFoundationMicroVG $Label_MicroVG $Label_MicroVG
union LibAddonMWT $Label_MWT $Label_MWT
union LibFoundationTrace $Label_Trace $Label_Trace
union Domino $Label_Domino $Label_Domino
union LibFoundationNUM $Label_NUM $Label_NUM
union LibAddonNETExt $Label_NETEXT $Label_NETEXT
union LibAddonRESTClient $Label_REST $Label_REST
union LibAddonObservable $Label_Observable $Label_Observable
union LibAddonWidget $Label_Widget $Label_Widget
union LibAddonOSGiME $Label_OSGiME $Label_OSGiME
union LibAddonNLS $Label_NLS $Label_NLS
union LibFoundationSNI $Label_SNI $Label_SNI
union LibFoundationSP $Label_SP $Label_SP
union LibAddonStoryBoard $Label_StoryBoard $Label_StoryBoard
union Internet_Pack $Label_Internet_Pack $Label_Internet_Pack
union LibAddonLogging $Logging $Logging
union LibAddonBasictool $Basictool $Basictool
union LibAddonComponents $Components $Components
union LibAddonService $Service $Service
union LibAddonWadapps $Wadapps $Wadapps
union LibAddonRCommand $RCommand $RCommand
union LibAddonWebsocket $Websocket $Websocket
union LibAddonSNTPClient $SNTPClient $SNTPClient
union LibAddonMotion $Motion $Motion
union LibAddonGesture $Gesture $Gesture
union LibAddonFlowMWT $FlowMWT $FlowMWT
union LibAddonFlow $Flow $Flow
union LibAddonAllJoyn $AllJoyn $AllJoyn
union LibAddonJSON $Label_JSON $Label_JSON
union LibAddonCBOR $Label_CBOR $Label_CBOR
union LibAddonMQTT $Label_Paho $Label_Paho
union LibAddonCoAP_Core $Label_Californium $Label_Californium
union LibAddonCoAP_Core_Connector_UDP $Label_Californium_Connector_UDP $Label_Californium_Connector_UDP
union LibAddonCoAP_Blockwise $Label_Californium_Blockwise $Label_Californium_Blockwise
union LibAddonCoAP_Observe $Label_Californium_Observe $Label_Californium_Observe
union LibAddonCoAP_Deduplication_Crop $Label_Californium_Deduplication_Crop $Label_Californium_Deduplication_Crop
union LibAddonCoAP_Deduplication_Sweep $Label_Californium_Deduplication_Sweep $Label_Californium_Deduplication_Sweep
union LibAddonLWM2M_Models $Label_Lwm2m_Models $Label_Lwm2m_Models
union LibAddonLWM2M_Impl_Californium $Label_Lwm2m_Californium $Label_Lwm2m_Californium
union LibAddonLWM2M_API $Label_Lwm2m_API $Label_Lwm2m_API
#union LibAddonHTTP $Label_HTTPClient $Label_HTTPClient
union TestSuite $TestSuite $TestSuite
union Drivers $Label_Drivers $Label_Drivers
union CoreEngineAllocator $Label_VMAllocator $Label_VMAllocator
union CoreEngine $Label_VMCore $Label_VMCore
union InstalledFeatures $Label_InstalledFeatures $Label_InstalledFeatures
union ApplicationFonts $Label_ApplicationFonts $Label_ApplicationFonts
union ApplicationImages $Label_ApplicationImages $Label_ApplicationImages
union ApplicationResources $Label_ApplicationResources $Label_ApplicationResources
union ApplicationImmutables $Label_ApplicationImmutables $Label_ApplicationImmutables
union ApplicationCode $Label_JavaApplication $Label_JavaApplication
union MiscRuntime $Label_MiscRuntime $Label_MiscRuntime
union BSP $Remaining $Remaining
union NativeMathFloatDouble $Label_LIBFloat $Label_LIBFloat
union NativeMathIntLong $Label_LIBInt $Label_LIBInt
union NativePrintfScanf $Label_PrintfScanf $Label_IceteaString
union NativeStackECOM_COMM $Label_NativeStackECOMCOMM $Label_NativeStackECOMCOMM
union NativeStackSSL $Label_NativeStackSSL $Label_NativeStackSSL
union NativeStackDTLS $Label_NativeStackDTLS $Label_NativeStackDTLS
union NativeStackDevice $Label_NativeStackDevice $Label_NativeStackDevice
union NativeStackSecurity $Label_NativeStackDevice $Label_NativeStackSecurity
union NativeStackMicroUI $Label_NativeStackMicroUI $Label_NativeStackMicroUI
union NativeStackNET $Label_NativeStackNET $Label_NativeStackNET
union NativeStackFS $Label_NativeStackFS $Label_NativeStackFS
union NativeStackWIFI $Label_NativeStackWifi $Label_NativeStackWifi
union NativeStackWifiTICC3100 $Label_NativeStackWifiTICC3100 $Label_NativeStackWifiTICC3100
union NativeStackWifiESP32 $Label_NativeESP32_WIFI $Label_NativeESP32_WIFI
union NativeStacBLEESP32 $Label_NativeESP32_BT $Label_NativeESP32_BT
union NativeStackModem3G $Label_NativeStackModem3G $Label_NativeStackModem3G
union NativeStackUSB $Label_Usb $Label_Usb

union ClassNames $Label_ClassNames $Label_ClassNames
union RuntimeTables $Label_RuntimeTables $Label_RuntimeTables
union RTOS $Label_OS $Label_OS
union BSPMalloc $Label_BSP_Malloc $Label_BSP_Malloc

# Print results
totalImageSize All

echo
echo APPLICATION:
totalImageSize ApplicationCode
totalImageSize ApplicationFonts
totalImageSize ApplicationImages
totalImageSize ApplicationResources
totalImageSize ApplicationImmutables
totalImageSize ApplicationStrings
totalImageSize ClassNames
totalImageSize RuntimeTables

echo
echo Java LIBRARIES:
totalImageSize LibFoundationEDC
totalImageSize LibFoundationBON
totalImageSize LibFoundationKF
totalImageSize LibFoundationFS
totalImageSize LibFoundationHAL
totalImageSize LibFoundationSSL
totalImageSize LibFoundationDTLS
totalImageSize LibFoundationSecurity
totalImageSize LibFoundationDevice
totalImageSize LibFoundationPAP
totalImageSize LibFoundationECOM
totalImageSize LibFoundationNET
totalImageSize LibFoundationBLUETOOTH
totalImageSize LibFoundationMicroUI
totalImageSize LibFoundationMicroVG
totalImageSize LibAddonMWT
totalImageSize LibFoundationTrace
totalImageSize LibFoundationNUM
totalImageSize LibAddonOSGiME
totalImageSize LibAddonNLS
totalImageSize LibFoundationSP
totalImageSize LibAddonStoryBoard
totalImageSize LibAddonWidget
totalImageSize LibAddonLogging
totalImageSize LibAddonBasictool
totalImageSize LibAddonComponents
totalImageSize LibAddonService
totalImageSize LibAddonWadapps
totalImageSize LibAddonRCommand
totalImageSize LibAddonWebsocket
totalImageSize LibAddonSNTPClient
totalImageSize LibAddonMotion
totalImageSize LibAddonGesture
totalImageSize LibAddonFlowMWT
totalImageSize LibAddonFlow
totalImageSize LibAddonAllJoyn
totalImageSize TestSuite
totalImageSize InstalledFeatures


echo
echo MicroJvm LIBRARIES & RUNTIME:
totalImageSize CoreEngine
totalImageSize CoreEngineAllocator
totalImageSize VMConsole
totalImageSize VMDebugger
totalImageSize Drivers

echo
echo BSP
totalImageSize BSP
totalImageSize NativeStackSSL
totalImageSize NativeStackNET
totalImageSize NativeStackDTLS
totalImageSize NativeStackDevice
totalImageSize NativeStackSecurity
totalImageSize NativeStackFS
totalImageSize NativeMathFloatDouble
totalImageSize NativeMathIntLong
totalImageSize NativePrintfScanf
totalImageSize RTOS

