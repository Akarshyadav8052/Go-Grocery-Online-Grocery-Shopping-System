import React, { useEffect, useState } from 'react'
import { useForm } from 'react-hook-form';

export default function FilterNavbar(props){
  const { register, handleSubmit, formState, watch } = useForm();
  let [categories, setCategories] = useState(null);
  const selectedCategoryId = watch("category");
      console.log(categories);
      async function fetchCategories() {
          let response = await fetch("http://localhost:8080/api/v1/get/categories");
          let responseObject = await response.json();
          setCategories(responseObject.data);
      }
  
      useEffect(() => {
          fetchCategories();
      }, []);

      //filtretion logic 
      let filterProducts=props.filterProductsFunction
      let[categoryName,setCategoryName]=useState(null)
      let[subCategoryName,setSubCategorName]=useState(null)
      let[sortDirection,setsortDirection]=useState(null)
      let[productName,setProductName]=useState(null)

      function collecFormData(formData) //this is used to collect product only
      {
        console.log(formData);
        setProductName(formData.productName) 
      } 

      useEffect(()=>
        {
            filterProducts(categoryName,subCategoryName,sortDirection,productName)
        },[categoryName,subCategoryName,sortDirection,productName])
  return (
    <div className='row mt-3 mb-3'>
        <div className="col-3">
           <select className="form-select" {...register("category",
            {
                onChange:(event)=>
                {
                    console.log(event.target.options[event.target.selectedIndex].text)
                    setCategoryName(event.target.options[event.target.selectedIndex].text)
                    setSubCategorName(null) 
                }
            }
           )}>
                <option value="">Select Category</option>
                <option value="All">All</option>
                {
                // categories?"yes":"Loading categories"
                categories
                    ? categories.map((category) => {
                        return (
                        <option value={category.id} key={category.id}>
                            {category.name}
                        </option>
                        );
                    })
                    : "Loading categories"
                }
            </select>
        </div>
        <div className="col-3">
          <select className="form-select" {...register("subCategory",
            {
                onChange:(event)=>
                {
                    console.log(event.target.options[event.target.selectedIndex].text)
                    setSubCategorName(event.target.options[event.target.selectedIndex].text)
                }
            }
          )}>
                <option value="">Select Sub Category</option>
                <option value="All">All</option>
                {categories
                ?.find((category) => category.id == selectedCategoryId)
                ?.subCategories?.map((subcategory) => (
                    <option value={subcategory.id} key={subcategory.id}>
                    {subcategory.name}
                    </option>
                ))}
            </select>
        </div>
        <div className="col-3">
          <select className="form-select" {...register("sort",
          {
                onChange:(event)=>
                {
                    console.log(event.target.value)
                    setsortDirection(event.target.value)
                }
            }

          )}>
              <option value="">Sort  By Price</option>
              <option value="All">Reset</option>
              <option value="desc">High to Low</option>
              <option value="asc">Low to High</option>
            </select>
        </div>
        <div className="col-3 ">
          <form className='d-flex ' onSubmit={handleSubmit(collecFormData)} >
                <input type="text" class="form-control me-2" placeholder='Product Name'
                {...register("productName")}/>
              
              <button type="submit" class="btn btn-primary">Search</button>
          </form>
        </div>
    </div>
  )
}
