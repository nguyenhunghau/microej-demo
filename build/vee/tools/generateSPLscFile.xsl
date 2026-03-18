<?xml version="1.0" encoding="UTF-8"?>
<!--
	Copyright 2010 IS2T. All rights reserved.
	IS2T PROPRIETARY. Use is subject to license terms.
	
	XSL file to generate LSC file from SP definition file. 
	All database symbols are defined as root symbols
	Bibliography:
		[XSLTREC] XSL Transformations (XSLT), Version 1.0, W3C Recommendation 16 November 1999
-->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	
	<!-- This is an XML to XML generator -->
	<xsl:output	method="xml" indent="yes"/>

	<xsl:template match="database">
		<xsl:element name="rootSymbol">
			<xsl:attribute name="name">shieldedplug_database_<xsl:value-of select="@id"/></xsl:attribute>
		</xsl:element>
	</xsl:template>
		
	<xsl:template match="shieldedPlug">
		<xsl:element name="lscFragment">
			<xsl:apply-templates/>
		</xsl:element>
	</xsl:template>
	
</xsl:stylesheet>
