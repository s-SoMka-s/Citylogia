using Core.Api.Auth.Models.Input;
using Core.Api.Auth.Models.Output;
using System.Threading.Tasks;

namespace Core.Api.Services
{
    public interface IUserService
    {
        Task<AuthenticateResponse> Authenticate(LoginParameters parameters);
    }
}
