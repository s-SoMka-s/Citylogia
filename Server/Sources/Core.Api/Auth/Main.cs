using Citylogia.Server.Core.Db.Implementations;
using Citylogia.Server.Core.Entityes;
using Core.Api.Auth.Models.Input;
using Core.Tools.Interfaces.Auth;
using Libraries.Auth;
using Microsoft.AspNetCore.Mvc;
using System.Linq;
using System.Threading.Tasks;

namespace Core.Api.Auth
{
    [ApiController]
    [Route("/api/Auth")]
    public class Main : ApiController
    {
        private readonly SqlContext context;
        private readonly IJwtManager jwtManager;

        public Main(SqlContext context, IJwtManager jwtManager)
        {
            this.context = context;
            this.jwtManager = jwtManager;
        }


        [HttpPost("Register")]
        public async Task<TokenPair> RegisterAsync([FromBody] RegisterParameters parameters)
        {
            var existed = this.context.Users.ToList().Any(u => u.Email == parameters.Email);
            if (existed)
            {
                return null;
            }

            var @new = parameters.Build();

            var users = this.context.Users;
            var user = await users.AddAsync(@new);
            await this.context.SaveChangesAsync();

            var tokenPair = await jwtManager.GeneratePairAsync(user.Entity.Id);

            return tokenPair;
        }

        [HttpPost("Email")]
        public async Task<TokenPair> LoginAsync([FromBody] LoginParameters parameters)
        {
            var user = this.context.Users.FirstOrDefault(u => u.Email == parameters.Email);

            var res = CheckUser(user, parameters.Password);

            if (!res)
            {
                return null;
            }

            var tokenPair = await jwtManager.GeneratePairAsync(user.Id);

            return tokenPair;
        }




        private bool CheckUser(User user, string password)
        {
            if (default == user)
            {
                return false;
            }

            if (!PasswordHandler.CheckPassword(password, user.Password))
            {
                return false;
            }

            return true;
        }
    }
}
