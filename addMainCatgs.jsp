<%@include file="./common/admHeader.jsp"%>
<%@include file="./common/admNav.jsp"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<style>
.custom_error{
	color: red;
    font-size: 12px;
    font-weight: bold;
}

</style>
<div class="container-fluid">

	<!-- Page Heading -->
	<div class="d-sm-flex align-items-center justify-content-between mb-4">
		<h1 class="h3 mb-0 text-gray-800">Main Categories</h1>

	</div>

	<!-- DataTales Example -->
	<div class="card shadow mb-4">
		<div class="card-header py-3">
			<h6 class="m-0 font-weight-bold text-primary">Add Main
				Categories List</h6>
		</div>
		<div class="card-body">
			<!-- <div class="table-responsive"> -->
			<div class="">

				<div class="row">
					<div class="col-sm-2"></div>
					<div class="col-sm-8">
						<c:if test="${not empty errorMesg}">
						  <span class="star_color"> Error: ${errorMesg}</span> 
						</c:if>
						<form:form action="saveMainCatergories" method="post"
							modelAttribute="catg" enctype="multipart/form-data" >
					<input type="hidden" name="${_csrf.parameterName}"	value="${_csrf.token}" />							
							<form:hidden path="ors_mc_id" />
							<form:hidden path="ors_mc_imageName" />
							<div class="row">
								<div class="col-md-3 py-md-2">
								 	
									<div class="form-group">
										<form:label path="ors_mc_category_name"><span class="star_color">*</span> Main Category Name:</form:label>
										
									</div>
									
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<form:input type="text" class="form-control form-control-user"
											aria-describedby="" name="loginId"
											placeholder="Main Category Name" path="ors_mc_category_name" />
									</div>
									<span class="custom_error" ><form:errors path="ors_mc_category_name"  cssStyle="custom_error"/></span>
								
								</div>
							</div>
							<div class="row">
								<div class="col-md-3 py-md-2">
									<div class="form-group">
										<form:label path="ors_order">Assigned order</form:label>
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
											<form:select path="ors_order" class="form-control"> 
									<form:option value="A" label="Select" />
									<form:options items="${numeric}" />
								</form:select>
									</div>
								</div>
							</div>
							<%-- <div class="row">
								<div class="col-md-3 py-md-2">
									<div class="form-group">
										Categories Image
									</div>
								</div>
								<div class="col-md-6">
									<div class="form-group">
										<form:input type="file" path="catgImage" id="catgImage"
											name="catgImage" class="form:input-large" />
									</div>
								</div>
							</div> --%>
							
<!-- 							Image          -->
							<div class="row">
								<div class="col-md-3 py-md-2">
									<div class="form-group text-right">
										Categories Image
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group">
										<form:input type="file" path="catgImage" id="catgImage"
											name="catgImage" class="form:input-large" />
									</div>
								</div>
								<div class="col-md-3">
									<div class="form-group text-center" style="font-weight: bold;color: black;">
									<c:if test="${catg.ors_mc_imageName!=null && !empty  catg.ors_mc_imageName && catg.ors_mc_imageName!='' }"> 
											<img
									 src="<c:url value='/resources/images/Categories/${catg.ors_mc_imageName}.png'/>" style="width: 100px;" />
									</c:if>
									</div>
								</div>
								<div class="col-md-1">
									<div class="form-group">
											<c:if test="${catg.ors_mc_imageName!=null && !empty  catg.ors_mc_imageName && prod.ors_mc_imageName!='' }"> 
										<a
												href="deleteMainImage?id=<c:out value="${catg.ors_mc_id}"/>&mainImg=<c:out value="${catg.ors_mc_imageName}"/>"><i
													class="fas fa-trash" style="color: red"  onclick="if (! confirm('Do you want to delete this  image ?')) { return false; }"></i></a>
									</c:if>
									</div>
								</div>
								
								
							</div>
							
							
							
							
							
							
							
							
							
							
							<div class="row">
								<div class="col-md-3 py-md-2">
								</div>
								<div class="col-md-6">
								<input type="submit" value="Submit" class="btn btn-success">
								</div>
							</div>
			
							
						</form:form>
					</div>
					<div class="col-sm-2"></div>
				</div>

			</div>
		</div>
	</div>

</div>





</div>
<!-- End of Main Content -->
<%@include file="./common/admFooter.jsp"%>