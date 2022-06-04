package com.cj.dentalclinic.exception

class ResourceNotFoundException(resource: String, id: Int?) : RuntimeException("No $resource found with id : $id")
