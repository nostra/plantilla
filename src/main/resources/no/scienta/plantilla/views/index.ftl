<#-- @ftlvariable name="" type="no.scienta.plantilla.views.GeneralView" -->
<#include "top.ftl" />

<section id="content" class="body">
    <#if lines??>
        <ul>
        <#list lines as line>
            <li>${line}</li>
        </#list>
        </ul>
    <#else>
        (Nothing here yet)
    </#if>



</section>
<#include "bottom.ftl" />
