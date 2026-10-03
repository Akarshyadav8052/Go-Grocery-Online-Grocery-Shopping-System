import React from 'react'
import VendorNavbar from './VendorNavbar'
import { Outlet } from 'react-router-dom'

export default function VendorDashboard() {
  let loggedInUser=JSON.parse(localStorage.getItem("user"))
  return (
    <div>
      <VendorNavbar/>
      welcome {loggedInUser.username}
      <Outlet/>
    </div>
  )
}
