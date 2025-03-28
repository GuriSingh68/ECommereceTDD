'use client';

import React from 'react';

const AboutUs = () => {
  return (
    <div className="min-h-screen bg-gray-50">
      {/* Hero Section */}
      <div className="bg-green-600 text-white py-16 px-6 text-center mt-[80px] lg:mt-[80px]">
        <h1 className="text-4xl font-extrabold mb-4">About Us</h1>
        <p className="text-lg">
          Discover who we are, what drives us, and why we’re committed to delivering the best.
        </p>
      </div>

      {/* Our Story Section */}
      <section className="container mx-auto px-6 py-12">
        <div className="flex flex-col md:flex-row items-center gap-8">
          <div className="w-full md:w-1/2">
            <h2 className="text-3xl font-bold text-gray-800 mb-4">
              Our <span className="text-green-600">Story</span>
            </h2>
            <p className="text-gray-600">
              Founded with a passion for sustainable farming, we aim to connect farmers and
              consumers in a way that promotes healthy living and environmental stewardship.
              Our journey began with a single idea: to provide fresh, organic, and locally
              sourced products to everyone.
            </p>
          </div>
          <div className="w-full md:w-1/2">
            <img
              src="/pexels-tomfisk-1595104.jpg"
              alt="Our Story"
              className="w-full h-auto rounded-lg shadow-lg"
            />
          </div>
        </div>
      </section>

      {/* Mission and Vision Section */}
      <section className="bg-gray-100 py-12">
        <div className="container mx-auto px-6 text-center">
          <div className="grid grid-cols-1 md:grid-cols-2 gap-12">
            <div>
              <h3 className="text-2xl font-bold text-gray-800 mb-4">
                Our <span className="text-green-600">Mission</span>
              </h3>
              <p className="text-gray-600">
                To deliver fresh, organic, and sustainable products while empowering local
                farmers and fostering healthier communities.
              </p>
            </div>
            <div>
              <h3 className="text-2xl font-bold text-gray-800 mb-4">
                Our <span className="text-green-600">Vision</span>
              </h3>
              <p className="text-gray-600">
                To create a world where every meal contributes to the well-being of people and
                the planet.
              </p>
            </div>
          </div>
        </div>
      </section>

      {/* Meet the Team Section */}
      <section className="flex flex-col items-center container mx-auto px-6 py-12">
        <h2 className="text-3xl font-bold text-center text-gray-800 mb-8">
          Meet Our <span className="text-green-600">Team</span>
        </h2>
        <div className="grid grid-cols-1 sm:grid-cols-3 md:grid-cols-3 gap-8">
          {/* Example Team Member */}
          <div className="text-center">
            <img
              src="/pexels-jakeheinemann-1482101.jpg"
              alt="Team Member"
              className="w-32 h-32 mx-auto rounded-full shadow-lg mb-4"
            />
            <h3 className="text-lg font-bold text-gray-800">Gurwinder Singh</h3>
            <p className="text-gray-600">CEO & Founder</p>
          </div>

          <div className="text-center">
            <img
              src="/pexels-jakeheinemann-1482101.jpg"
              alt="Team Member"
              className="w-32 h-32 mx-auto rounded-full shadow-lg mb-4"
            />
            <h3 className="text-lg font-bold text-gray-800">Jay Shah</h3>
            <p className="text-gray-600">CEO & Co-Founder</p>
          </div>

          <div className="text-center">
            <img
                src="/pexels-jakeheinemann-1482101.jpg"
                alt="Team Member"
                className="w-32 h-32 mx-auto rounded-full shadow-lg mb-4"
            />
            <h3 className="text-lg font-bold text-gray-800">Lina Syed</h3>
            <p className="text-gray-600">CFO</p>
          </div>
        </div>
      </section>

      {/* Values Section */}
      <section className="bg-green-600 text-white py-16 px-6">
        <div className="container mx-auto text-center">
          <h2 className="text-3xl font-bold mb-8">Our Core Values</h2>
          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            <div>
              <h3 className="text-2xl font-bold mb-2">Sustainability</h3>
              <p>We are committed to practices that protect and preserve the planet.</p>
            </div>
            <div>
              <h3 className="text-2xl font-bold mb-2">Integrity</h3>
              <p>Transparency and trust guide every decision we make.</p>
            </div>
            <div>
              <h3 className="text-2xl font-bold mb-2">Community</h3>
              <p>We believe in supporting farmers, consumers, and local economies.</p>
            </div>
          </div>
        </div>
      </section>

      {/* Call to Action Section */}
      <section className="py-12">
        <div className="container mx-auto px-6 text-center">
          <h2 className="text-3xl font-bold text-gray-800 mb-4">
            Join Us on Our Journey
          </h2>
          <p className="text-gray-600 mb-8">
            Become a part of our mission to make fresh, organic, and sustainable living accessible to all.
          </p>
          <button className="bg-green-600 text-white px-6 py-3 rounded-lg shadow hover:bg-green-700 transition">
            Learn More
          </button>
        </div>
      </section>
    </div>
  );
};

export default AboutUs;