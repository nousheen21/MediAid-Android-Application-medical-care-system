package com.bekar.smartmedicalcare.Activity;

public class HM_Element {
    private String elementId;
    private String elementName;
    private String elementInfo;
    private String elementDept;

    public HM_Element() {
    }

    public HM_Element(String elementId, String elementName, String elementInfo, String elementDept) {
        this.elementId = elementId;
        this.elementName = elementName;
        this.elementInfo = elementInfo;
        this.elementDept = elementDept;
    }

    public String getElementId() {
        return elementId;
    }

    public String getElementName() {
        return elementName;
    }

    public String getElementInfo() {
        return elementInfo;
    }

    public String getElementDept() {
        return elementDept;
    }
}
