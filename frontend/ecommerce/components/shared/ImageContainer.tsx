const ImageContainer = () => {
    return (
        <div className="image-container fixed top-0 left-0 w-full h-screen z-[-1]">
            <img src="/main.jpg" alt="Main Background" className="w-full h-full object-cover" />
        </div>
    );
};

export default ImageContainer;