import Link from "next/link"
import { FaStar } from "react-icons/fa6"
import { LuBriefcaseBusiness } from "react-icons/lu"
import { Tag1 } from "../tag/Tag1"


export const ProfileCard = () => {
  return (
    <Link
      href="#"
      className="p-[24px] rounded-[16px] border border-[#e9e9e9] block hover:border-blue-600 hover:shadow-xl transition-all duration-300"
    >
      <div
        className="flex items-center gap-[16px]"
      >
        <img
          src="https://www.andrewng.org/_next/image?url=%2Fimages%2Fandrew-ng-about-page.jpg&w=750&q=75&dpl=dpl_6bohFvstLwgAVoY3TgnriRTNxNkR" 
          alt="" 
          className="w-[60px] h-[60px] rounded-[50%] object-cover"
        />
        <div
          className="flex-1"
        >
          <div
            className="text-[22px] font-[600] mb-[4px] text-[#121212]"
          >
            Atul K.
          </div>
          <div
            className="text-[16px] font-[500] text-[#676767]"
          >
            Noida, India
          </div>
          <div
            className="mt-[4px] flex gap-x-[12px] gap-y-[5px] items-center flex-wrap"
          >
            <div
              className="text-[#121212] text-[16px]"
            >
              $30/hr
            </div>
            <div
              className="flex gap-[4px] items-center"
            >
              <FaStar className="text-[24px] text-[#df7606]" /> 5.0
            </div>
            <div
              className="flex gap-[4px] items-center "
            >
              <LuBriefcaseBusiness className="text-[24px]" /> 168 jobs
            </div>
          </div>
        </div>
      </div>
      <div
        className="mt-[16px] line-clamp-3 font-[400] text-[16px] text-[#676767]"
      >
        <p>
          AI Engineer & Machine Learning Specialist với hơn 7 năm kinh nghiệm trong lĩnh vực Trí tuệ Nhân tạo, Machine Learning và Deep Learning. Đã tham gia phát triển nhiều hệ thống AI phục vụ hàng triệu người dùng trong các lĩnh vực E-commerce, Fintech và Healthcare.
        </p>
        <p>
          Thành thạo các công nghệ như Python, TensorFlow, PyTorch, Scikit-learn, LangChain, OpenAI API, Hugging Face, Vector Database (Pinecone, Weaviate) và các nền tảng Cloud như AWS, Google Cloud Platform và Microsoft Azure.
        </p>
        <p>
          Có kinh nghiệm xây dựng các giải pháp Chatbot AI, AI Agent, Recommendation System, Computer Vision, NLP, RAG System và Generative AI Applications. Đã triển khai thành công hơn 50 dự án AI cho doanh nghiệp và startup trên toàn cầu.
        </p>
      </div>
      <div
        className="mt-[16px] flex gap-[8px]"
      >
        <Tag1
          label="AI Chatbot"
        />
      </div>
      <button
        className="mt-[16px] px-[16px] h-[40px] bg-[#000065] hover:bg-[#000096] text-white block flex items-center justify-center rounded-[8px] w-full"
      >
        Xem thông tin chi tiết
      </button>
    </Link>
  )
}