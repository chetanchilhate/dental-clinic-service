package com.cj.dentalclinic.dto

import com.cj.dentalclinic.entity.BillDetailView

class BillDetailDto(val billDetail: BillDetailView, val billLines: List<BillLineDto>)
