using Core.Tools;
using Microsoft.AspNetCore.Mvc;

namespace Core.Api
{
    public class ApiController : Controller
    {
        public long GetUserId()
        {
            if (!HttpContext.User.TryGetId(out var userId)){
                return 0;
            }

            return userId;
        }
    }
}
