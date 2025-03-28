export default function Footer() {
  return (
    <footer className="bg-navbar-bg text-white py-6 mt-4">
      <div className="container mx-auto flex flex-col md:flex-row justify-center items-center">
        <div className="text-center text-sm">
          © {new Date().getFullYear()} FarmKart. All rights reserved.
        </div>
      </div>

    </footer>
  )
}