<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<table class="ui-widget ui-widget-content">
	<tr>
		<td width="50%"><span id="offsetFrom"></span>&nbsp;to&nbsp;<span
			id="offsetTo"></span>&nbsp;of&nbsp;<span id="totalRecords"></span></td>
		<td style="text-align: right;"><span style="white-space: nowrap;">&nbsp;<a
				id="previousPage" style="font-weight: bold; cursor: pointer;"
				title="Previous"><i class="fas fa-step-backward"></i></a>
				&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp; <a id="nextPage"
				style="font-weight: bold; cursor: pointer;" title="Next"><i
					class="fas fa-step-forward"></i></a>&nbsp;
		</span> <input type="number" id="pageCounter" value="1"></td>
	</tr>
</table>
