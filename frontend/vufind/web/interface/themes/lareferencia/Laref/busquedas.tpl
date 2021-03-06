<h1>{translate text="Impacto"}</h1>  
<div class="span-18 last">
  <h2>{translate text="B&uacute;squedas"}</h2>
  <h3>{translate text="Resumen"}</h3>
  <table class="citation">
  <tr>
    <th>{translate text="Total de B&uacute;squedas"}: </th>
    <td>{$searchCount}</td>
  </tr>
  <tr>
    <th>{translate text="B&uacute;squedas sin resultado "}: </th>
    <td>{$nohitCount}</td>
  </tr>
  <tr>
    <th>{translate text="Total de Consultas"}: </th>
    <td>{$recordViews}</td>
  </tr>
  </table>

  <h2>{translate text="B&uacute;squedas m&aacute;s frecuentes"}</h2>
  <ul>
  {foreach from=$termList item=term}
    <li><a href="{$url}/Search/Results?&type=AllFields&submit=Buscar&lookfor={$term.0|escape}">({$term.1}) {$term.0|escape}</a></li>
  {foreachelse}
    <li>{translate text="No Searches"}</li>
  {/foreach}
  </ul>
  
</div>

<div class="clear"></div>